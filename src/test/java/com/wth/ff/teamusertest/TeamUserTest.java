package com.wth.ff.teamusertest;

import com.wth.ff.model.domain.UserTeam;
import com.wth.ff.service.UserTeamService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

/**
 *
 */
@SpringBootTest
public class TeamUserTest {

    @Resource
    private UserTeamService userTeamService;

    @Test
    void test1() {
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(1L);
        userTeam.setTeamId(2L);
        userTeam.setJoinTime(new Date());
        boolean save = userTeamService.save(userTeam);
        System.out.println(save);

    }


}
