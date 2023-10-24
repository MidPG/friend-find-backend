package com.wth.ff.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wth.ff.model.domain.UserTeam;
import com.wth.ff.service.UserTeamService;
import com.wth.ff.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
* @author 79499
* @description 针对表【user_team(用户-队伍表)】的数据库操作Service实现
* @createDate 2023-09-22 19:43:22
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}




