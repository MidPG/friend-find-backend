package com.wth.ff.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 队伍
 * @TableName team
 */
@TableName(value ="team")
@Data
public class Team implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 队伍名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 队伍描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 最大人数
     */
    @TableField(value = "maxNum")
    private Integer maxNum;

    /**
     * 过期时间
     */
    @TableField(value = "expireTime")
    private Date expireTime;

    /**
     * 创建用户id
     */
    @TableField(value = "userId")
    private Long userId;

    /**
     * 队伍状态 0 - 公开 1 - 私有 2 - 加密
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 队伍密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "updateTime")
    private Date updateTime;

    /**
     * 是否删除（逻辑删除）
     */
    @TableField(value = "isDelete")
    private Integer isDelete;

    @TableLogic
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}