package com.wth.ff.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wth.ff.common.ErrorCode;
import com.wth.ff.exception.BusinessException;
import com.wth.ff.mapper.UserMapper;
import com.wth.ff.model.domain.User;
import com.wth.ff.service.UserService;
import com.wth.ff.utils.AlgorithmUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Pair;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.wth.ff.constant.UserConstant.*;

/**
* @author 79499
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-07-27 18:21:31
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private UserMapper userMapper;


    /**
     *  盐值，混淆密码
     */
    public static final String SALT = "wth";

    @Override
    public long UserRegister(String userAccount, String userPassword, String checkPassword, String studentId) {
        //1.非空校验
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账户过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (studentId.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "学号过长");
        }
        //使用不包含特殊字符的正则表达式：这个正则表达式只允许包含字母和数字的字符串，不包含任何特殊字符
        String validPattern = "^[a-zA-Z0-9]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.find()) {
            return -1;
        }
        //密码校验成功
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        //账户不能重复,细节：这个应该放在后面，节省查询效率
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
            return -1;
        }
        // 学号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("studentId",studentId);
        count = this.count(queryWrapper);
        if (count > 0) {
            return -1;
        }
        // 2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setStudentId(studentId);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
        return user.getId();

    }

    //request作用：往请求的session中设计值，从请求的session中读取值
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.登录信息校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4 || userPassword.length() < 6) {
            return null;
        }
        //账户不能包含特殊字符
        String validPattern = "^[a-zA-Z0-9]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.find()) {
            System.out.println("含有特殊字符");
            return null;
        }
        // 2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            return null;
        }
        // 3.用户脱敏
        User safetyUser = getSafetyUser(user);
        // 4.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {//shift + F6
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setStudentId(originUser.getStudentId());
        safetyUser.setTags(originUser.getTags());
        return safetyUser;
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除登录态
        HttpSession session = request.getSession();
        session.removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     *  根据内存查询（灵活）
     */
    public List<User> searchUsersByTags(List<String> tagNameList) {
        // 基于内存查询
        // 1.先查询所有用户
        System.out.println(tagNameList);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);
        // 2.在内存中判断是否包含要求的标签
        Gson gson = new Gson();
        return  userList.stream().filter(user -> {
            String tagsStr = user.getTags();
            if (StringUtils.isBlank(tagsStr)) {
                return false;
            }
            //进行json反序列化
            Type type = new TypeToken<Set<String>>(){}.getType();
            Set<String> tempTagNameSet = gson.fromJson(tagsStr, type);
            for (String tagName : tagNameList) {
                if (!tempTagNameSet.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).map(this::getSafetyUser).collect(Collectors.toList());
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return (User)userObj;
    }

    // todo 存在bug 需要优化
    @Override
    public int updateUser(User user, User loginUser) {
        long userId = user.getId();
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 如果登录用户是管理员 则可以修改所有人信息
        // 不是管理员只能修改自己信息
        if (!isAdmin (loginUser) && userId != loginUser.getId()){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        User oldUser = userMapper.selectById(user.getId());
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userMapper.updateById(user);
    }

    /**
     * 登录页index.html分页展示推荐用户
     */
    @Override
    public Page<User> recommendUser(long pageNum, long pageSize, HttpServletRequest request) {
        User loginUser = getLoginUser(request);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 先去缓存中查数据
        String key = USER_RECOMMEND_KEY + loginUser.getId();
        Page<User> userList = (Page<User>)valueOperations.get(key);
        if (userList != null) {
            return userList;
        }
        // 缓存中没有，去数据库中查数据
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        userList = this.page(new Page<>(pageNum, pageSize), queryWrapper);
        // 存到缓存中 并设置过期时间
        try{
            valueOperations.set(key, userList,30000, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            log.error("redis set key error");
        }
        return userList;
    }

    @Override
    public List<User> matchUsers(long num, User loginUser) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "tags");
        queryWrapper.isNotNull("tags");
        List<User> userList = this.list(queryWrapper);
        String tags = loginUser.getTags();
        Gson gson = new Gson();
        List<String> tagList = gson.fromJson(tags, new TypeToken<List<String>>() {
        }.getType());
        // 用户列表的下标 => 相似度
        List<Pair<User, Long>> list = new ArrayList<>();
        // 依次计算所有用户和当前用户的相似度
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            String userTags = user.getTags();
            // 无标签或者为当前用户自己
            if (StringUtils.isBlank(userTags) || user.getId() == loginUser.getId()) {
                continue;
            }
            List<String> userTagList = gson.fromJson(userTags, new TypeToken<List<String>>() {
            }.getType());
            // 计算分数
            long distance = AlgorithmUtils.minDistance(tagList, userTagList);
            list.add(new Pair<>(user, distance));
        }
        // 按编辑距离由小到大排序
        List<Pair<User, Long>> topUserPairList = list.stream()
                .sorted((a, b) -> (int) (a.getValue() - b.getValue()))
                .limit(num)
                .collect(Collectors.toList());
        // 原本顺序的 userId 列表
        List<Long> userIdList = topUserPairList.stream().map(pair -> pair.getKey().getId()).collect(Collectors.toList());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("id", userIdList);
        // 1, 3, 2
        // User1、User2、User3
        // 1 => User1, 2 => User2, 3 => User3
        Map<Long, List<User>> userIdUserListMap = this.list(userQueryWrapper)
                .stream()
                .map(user -> getSafetyUser(user))
                .collect(Collectors.groupingBy(User::getId));
        List<User> finalUserList = new ArrayList<>();
        for (Long userId : userIdList) {
            finalUserList.add(userIdUserListMap.get(userId).get(0));
        }
        return finalUserList;
    }


    /**
     *  判断是否是管理员 通过已登录用户
     */
    public boolean isAdmin (User loginUser) {
        return loginUser != null && loginUser.getUserRole() == 1;
    }


    /**
     *  判断是否是管理员 通过HttpRequest
     */
    public boolean isAdmin(HttpServletRequest request) {
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObject;
//        if (user == null || user.getUserRole() != ADMIN_ROLE) {
//            return false;
//        }
//        return true;
        //上面代码更好的写法
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }


    /**
     *  根据sql条件进行查询
     */
    @Deprecated
    public List<User> searchUsersByTagsBySQL(List<String> tagNameList) {

        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //基于数据库查询
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        for (String tagName : tagNameList) {
            queryWrapper.like("tags", tagName);
        }
        List<User> userList = userMapper.selectList(queryWrapper);
        return  userList.stream().map(this::getSafetyUser).collect(Collectors.toList());
        //this::getSafetyUser == userList.forEach(user -> {getSafetyUser(user)});

    }




}



















