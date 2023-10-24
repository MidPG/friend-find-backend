package com.wth.ff.service;

import com.wth.ff.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void testAddUser(){
        User user = new User();
        user.setUsername("voidWth");
        user.setUserAccount("1722468552");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setUserPassword("xxxx");
        user.setPhone("12345");
        user.setEmail("123@qq.com");
        boolean result = userService.save(user);
    }

    @Test
    void userRegister() {
//        String userAccount = "";
//        String userPassword = "123456#";
//        String checkPassword = "123456";
//        // 空字符测试
//        long result = userService.UserRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, result);
//        // 账户字符长度测试
//        userAccount = "wt";
//        result = userService.UserRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, result);
//        // 账户含有特殊字符测试
//        userAccount = "shiwth#";
//        result = userService.UserRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, result);
//        // 密码和检验密码是否相同测试
//        userAccount = "shiwtha";
//        result = userService.UserRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, result);
//        userAccount = "wswth@";
//        userPassword = "12345678";
//        checkPassword = "12345678";
//        result = userService.UserRegister(userAccount, userPassword, checkPassword);
//        System.out.println(result);
    }

    @Test
     void testAdd(){

        String userAccount = "public";
        String userPassword = "12345678";
        String checkPassword = "12345678";
//        long i = userService.UserRegister(userAccount, userPassword, checkPassword);
//        System.out.println(i);
    }
    @Test
    void testLogin(){



    }


}













