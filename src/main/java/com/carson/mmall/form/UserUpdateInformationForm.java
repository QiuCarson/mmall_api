package com.carson.mmall.form;

import lombok.Data;
import org.hibernate.validator.constraints.Email;

@Data
public class UserUpdateInformationForm {

    private String username;

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
