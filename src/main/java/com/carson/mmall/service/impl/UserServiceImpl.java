package com.carson.mmall.service.impl;

import com.carson.mmall.common.Const;
import com.carson.mmall.dataobject.User;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.enums.RoleEnum;
import com.carson.mmall.exception.MmallException;
import com.carson.mmall.form.UserForm;
import com.carson.mmall.repository.UserRepository;
import com.carson.mmall.service.UserService;
import com.carson.mmall.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    @Override
    public User login(String username, String password) {
        User user = repository.findByUsername(username);

        if (user == null) {
            throw new MmallException(ResultEnum.USERNAME_NOT_EXISTS);

        }

        String md5Password = MD5Util.encode(password);

        if (!user.getPassword().equals(md5Password)) {
            throw new MmallException(ResultEnum.PASSWORD_ERROR);
        }
        return user;
    }

    @Override
    @Transactional
    public User register(UserForm userForm) {
        //检查用户是否存在
        User userInfo=repository.findByUsername(userForm.getUsername());
        if(userInfo != null){
            throw new MmallException(ResultEnum.USERNAME_EXISTS);
        }
        //密码MD5
        String password=MD5Util.encode(userForm.getPassword());
        User user=new User();
        user.setUsername(userForm.getUsername());
        user.setPassword(password);
        user.setEmail(userForm.getEmail());
        user.setPhone(userForm.getPhone());
        user.setQuestion(userForm.getQuestion());
        user.setAnswer(userForm.getAnswer());
        user.setRole(RoleEnum.USER.getCode());
        return repository.save(user);

    }

    @Override
    @Transactional
    public void check_username(String str, String type) {
        if(type.equals(Const.EMAIL)){
          User user=  repository.findByUsername(str);

        }else if(type.equals(Const.USERNAME)){

        }else{
            throw new MmallException(ResultEnum.PARAM_ERROR);
        }
    }

    @Override
    public User user_info(String username) {
        User user=  repository.findByUsername(username);
        return user;
    }
}
