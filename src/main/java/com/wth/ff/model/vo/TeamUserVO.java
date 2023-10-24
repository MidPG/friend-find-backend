package com.wth.ff.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 *  用户返回信息，在前端展示
 */
@Data
public class TeamUserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1350447498562604683L;

    private Long id;
    private String name;
    private String description;
    private Integer maxNum;
    private Date expireTime;
    private Long userId;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    /**
     * 创建人用户信息
     */
    private UserVO createUser;

    /**
     * 已加入的用户数
     */
    private Integer hasJoinNum;

    /**
     * 是否已加入队伍
     */
    private boolean hasJoin = false;
    
}
