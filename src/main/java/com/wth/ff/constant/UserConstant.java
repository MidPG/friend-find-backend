package com.wth.ff.constant;


/**
 *  用户常量
 *
 * @author 79499
 */
public interface UserConstant {

    /**
     *  用户登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";

    /**
     *  用户推荐redis key常量
     */
    String USER_RECOMMEND_KEY = "ff:user:recommend";


    //-------权限----------
    /**
     * 默认权限
     */
    int DEFAULT_ROLE = 0;

    /**
     * 管理员权限
     */
    int ADMIN_ROLE = 1;

}
