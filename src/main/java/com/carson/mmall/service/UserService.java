package com.carson.mmall.service;

import com.carson.mmall.dataobject.User;
import com.carson.mmall.form.UserForm;
import com.carson.mmall.form.UserUpdateInformationForm;

public interface UserService {
    User login(String username, String password);

    User register(UserForm userForm);

    void check_username(String str, String type);

    User user_info(String username);

    String forget_get_question(String username);

    String forget_check_answer(String username, String question, String answer);

    User forget_reset_password(String username, String passwordNew, String forgetToken);

    User reset_password(String username, String passwordOld, String passwordNew);

    User update_information(UserUpdateInformationForm user);

    User information(String username);
}
