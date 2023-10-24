package com.wth.ff.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wth.ff.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 79499
 * @description 针对表【user】的数据库操作Service
 * @createDate 2023-07-27 18:21:31
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    long UserRegister(String userAccount, String userPassword, String checkPassword, String studentId);


    /**
     * @param userAccount 用户账号
     * @param userPassword  用户密码
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 用户脱敏
     *
     * @param originUser 要脱敏的User对象
     * @return  脱敏后的User对象
     */
    User getSafetyUser(User originUser);

    /**
     *  用户退出
     */
    int userLogout(HttpServletRequest request);

    /**
     *  根据标签信息查找相关用户
     */
    List<User> searchUsersByTags(List<String> tagNameList);

    /**
     *  获取当前登录用户
     */
    User getLoginUser(HttpServletRequest request);

    /**
     *  更改用户信息
     */
    int updateUser (User user, User loginUser);

    /**
     * 分页查询推荐用户
     */
    Page<User> recommendUser(long pageNum, long pageSize, HttpServletRequest request);

    /**
     * 是否为管理员
     */
    boolean isAdmin (HttpServletRequest request);


    boolean isAdmin (User loginUser);


    List<User> matchUsers(long num, User loginUser);
}















