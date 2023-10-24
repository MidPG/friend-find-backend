package com.wth.ff.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 *  添加队伍请求类
 */
@Data
public class TeamAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 6956586913439078585L;

    private String name;

    private String description;

    private Integer maxNum;

    private Integer status;

    private String password;

    private Date expireTime;

}
