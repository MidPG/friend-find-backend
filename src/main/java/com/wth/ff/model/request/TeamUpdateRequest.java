package com.wth.ff.model.request;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 *  修改队伍请求类
 */
@Data
public class TeamUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 8785123422204025582L;

    private Long id;

    private String name;

    private String description;

    private Integer maxNum;

    private Integer status;

    private String password;

}