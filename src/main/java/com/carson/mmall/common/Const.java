package com.carson.mmall.common;

import com.carson.mmall.config.CustomConfig;
import org.springframework.beans.factory.annotation.Autowired;

public class Const {
    @Autowired
    private CustomConfig customConfig;

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    //登录之后保存的session
    public static final String SESSION_AUTH="username";
    public static final String SESSION_AUTH_ID="id";

    //购物车是否超过库存数量
    public static final String LIMIT_NUM_SUCCESS="LIMIT_NUM_SUCCESS";
    public static final String LIMIT_NUM_FAIL="LIMIT_NUM_FAIL";

    //
    public String IMAGEHOST=customConfig.getImageHost();

}
