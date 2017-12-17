package com.carson.mmall.service;

import com.carson.mmall.dataobject.User;
import com.carson.mmall.form.UserForm;

public interface UserService {
    User login(String username, String password);
    User register(UserForm userForm);
    void check_username(String str,String type);
    User user_info(String username);
}
