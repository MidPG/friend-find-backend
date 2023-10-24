package com.wth.ff.model.request;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author 79499
 */
@Data
public class UserRegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 2425286157252240109L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String studentId;

}
