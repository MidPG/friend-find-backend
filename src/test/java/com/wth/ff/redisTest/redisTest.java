package com.wth.ff.redisTest;
import java.util.Date;

import com.wth.ff.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

/**
 *  测试redis操作客户端
 */
@SpringBootTest
public class redisTest {

    @Resource
    private RedisTemplate redisTemplate;


    @Test
    public void test1() {

        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("wth", "qwe");
        valueOperations.set("abcd", "qwe");
        User user = new User();
        user.setId(0L);
        user.setUsername("");
        user.setUserAccount("");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setUserPassword("");
        user.setPhone("");
        user.setEmail("");
        user.setUserStatus(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);
        user.setUserRole(0);
        user.setStudentId("");
        user.setTags("");
        valueOperations.set("user", user);
        String s = (String) valueOperations.get("wth");
        System.out.println(valueOperations.get("user"));




    }

}
