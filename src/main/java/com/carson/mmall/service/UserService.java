package com.carson.mmall.service;

import com.carson.mmall.dataobject.User;
import com.carson.mmall.form.UserForm;
import com.carson.mmall.form.UserUpdateInformationForm;

public interface UserService {
    User login(String username, String password);

    User register(UserForm userForm);

    void checkUsername(String str, String type);

    User userInfo(Integer userId);

    String forgetGetQuestion(String username);

    String forgetCheckAnswer(String username, String question, String answer);

    User forgetResetPassword(String username, String passwordNew, String forgetToken);

    User resetPassword(String username, String passwordOld, String passwordNew);

    User updateInformation(UserUpdateInformationForm user);

    User information(Integer userId);
}
