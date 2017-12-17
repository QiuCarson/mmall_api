package com.carson.mmall.form;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UserForm {
    //用户名
    @NotEmpty(message="用户名不能为空！")
    private String username;

    //用户密码，MD5加密
    @NotEmpty(message="密码不能为空！")
    private String password;

    //确认密码
    private String passwordConfirm;

    //用户邮件地址
    @Email(message = "邮件地址格式不正确")
    private String email;

    //用户手机
    private String phone;

    //找回密码问题
    private String question;

    //找回密码答案
    private String answer;
}
