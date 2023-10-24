package com.wth.ff.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wth.ff.common.BaseResponse;
import com.wth.ff.model.domain.Team;
import com.wth.ff.model.domain.User;
import com.wth.ff.model.dto.TeamQuery;
import com.wth.ff.model.request.TeamAddRequest;
import com.wth.ff.model.request.TeamJoinRequest;
import com.wth.ff.model.request.TeamQuitRequest;
import com.wth.ff.model.request.TeamUpdateRequest;
import com.wth.ff.model.vo.TeamUserVO;

import java.util.List;

/**
* @author 79499
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2023-09-22 19:39:08
*/
public interface TeamService extends IService<Team> {

    Long addTeam(TeamAddRequest teamAddRequest, User loginUser);

    Boolean updateTeam(TeamUpdateRequest updateRequest, User loginUser);

    Boolean deleteTeamById(Long id, User loginUser);

    /**
     * 搜索队伍
     *
     * @param teamQuery
     * @param isAdmin
     * @return
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin);

    /**
     * 加入队伍
     *
     * @param teamJoinRequest
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * 退出队伍
     *
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);
}
