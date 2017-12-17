package com.carson.mmall.VO;

public class UserVO {

    //用户名
    private String username;

    //用户密码，MD5加密
    private String password;

    //用户邮件地址
    private String email;

    //用户手机
    private String phone;

    //找回密码问题
    private String question;

    //找回密码答案
    private String answer;

    //角色0-管理员,1-普通用户
    private Integer role;

}
