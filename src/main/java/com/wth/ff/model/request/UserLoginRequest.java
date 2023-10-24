package com.wth.ff.model.request;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 136297165785502149L;

    private String userAccount;

    private String userPassword;



}
