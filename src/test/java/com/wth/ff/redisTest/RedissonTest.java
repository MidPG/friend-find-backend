package com.wth.ff.redisTest;

import com.wth.ff.model.domain.User;
import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@SpringBootTest
public class RedissonTest {

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RedissonClient redissonClient;

    @Test
    public void test1(){

        RList<Object> list = redissonClient.getList("test-list");
        list.add("wth");
        list.add(1);
        list.add(new User());
        System.out.println("list:" + list.get(0));
        System.out.println("list:" + list.get(1));
        list.remove(0);
        System.out.println("list:" + list.get(2));
    }

    @Test
    public void test2() {

        HashOperations<String,Object,Object> hashOperations = redisTemplate.opsForHash();

        hashOperations.put("1", 123, "a");
        hashOperations.put("1", 124, "3");
    }


}
