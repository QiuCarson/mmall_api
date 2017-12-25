package com.carson.mmall.service.impl;

import com.carson.mmall.common.Const;
import com.carson.mmall.config.CustomConfig;
import com.carson.mmall.dataobject.User;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.enums.RoleEnum;
import com.carson.mmall.exception.MmallException;
import com.carson.mmall.form.UserForm;
import com.carson.mmall.form.UserUpdateInformationForm;
import com.carson.mmall.repository.UserRepository;
import com.carson.mmall.service.UserService;
import com.carson.mmall.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private CustomConfig customConfig;

    @Override
    public User login(String username, String password) {
        User user = repository.findTopByUsername(username);

        if (user == null) {
            throw new MmallException(ResultEnum.USERNAME_NOT_EXISTS);
        }

        String md5Password = getPasswordMD5(password);

        if (!user.getPassword().equals(md5Password)) {
            throw new MmallException(ResultEnum.PASSWORD_ERROR);
        }
        return user;
    }

    @Override
    @Transactional
    public User register(UserForm userForm) {
        //检查用户是否存在
        User userInfo = repository.findTopByUsername(userForm.getUsername());
        if (userInfo != null) {
            throw new MmallException(ResultEnum.USERNAME_EXISTS);
        }
        //密码MD5
        String password = getPasswordMD5(userForm.getPassword());
        User user = new User();
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
    public void checkUsername(String str, String type) {
        if (type.equals(Const.EMAIL)) {
            User user = repository.findTopByEmail(str);
            if(user!=null){
                throw new MmallException(ResultEnum.USERNAME_EMAIL_EXISTS);
            }
        } else if (type.equals(Const.USERNAME)) {
            User user = repository.findTopByUsername(str);
            if(user!=null){
                throw new MmallException(ResultEnum.USERNAME_EXISTS);
            }
        } else {
            throw new MmallException(ResultEnum.PARAM_ERROR);
        }
    }

    @Override
    public User userInfo(Integer userId) {
        User user = repository.findOne(userId);
        if (user == null) {
             throw new MmallException(ResultEnum.USERNAME_NOT_EXISTS);
        }
        user.setPassword(null);
        user.setQuestion(null);
        user.setAnswer(null);
        return user;
    }

    @Override
    public String forgetGetQuestion(String username) {
        User user = repository.findTopByUsername(username);
        if (user == null) {
            throw new MmallException(ResultEnum.USERNAME_NOT_EXISTS);
        }
        if (user.getQuestion().isEmpty()) {
            throw new MmallException(ResultEnum.USERNAME_NOT_QUESTION);
        }
        return user.getQuestion();
    }

    @Override
    public String forgetCheckAnswer(String username, String question, String answer) {
        User user = repository.findTopByUsername(username);
        if (user == null) {
            throw new MmallException(ResultEnum.USERNAME_NOT_EXISTS);
        }
        if (!user.getQuestion().equals(question)) {
            throw new MmallException(ResultEnum.QUESTION_ERROR);
        }
        if (!user.getAnswer().equals(answer)) {
            throw new MmallException(ResultEnum.ANSWER_ERROR);
        }
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("token" + user.getUsername(), token, 5 * 60, TimeUnit.SECONDS);
        return token;
    }

    @Override
    @Transactional
    public User forgetResetPassword(String username, String passwordNew, String forgetToken) {
        String redisToken = redisTemplate.opsForValue().get("token" + username);
        if (redisToken == null || !redisToken.equals(forgetToken)) {
            throw new MmallException(ResultEnum.TOKEN_ERROR);
        }
        User user = repository.findTopByUsername(username);
        if (user == null) {
            throw new MmallException(ResultEnum.USERNAME_NOT_EXISTS);
        }
        String password = MD5Util.encode(passwordNew);
        user.setPassword(password);
        return repository.save(user);
    }

    @Override
    @Transactional
    public User resetPassword(String username, String passwordOld, String passwordNew) {
        if (username.isEmpty()) {
            throw new MmallException(ResultEnum.USERNAME_NOT_AUTH);
        }
        User user = repository.findTopByUsername(username);
        if (user == null) {
            throw new MmallException(ResultEnum.USERNAME_NOT_EXISTS);
        }
        String password = MD5Util.encode(passwordOld);
        if (!user.getPassword().equals(password)) {
            throw new MmallException(ResultEnum.OLD_PASSWORD_ERROR);
        }
        String password2 = MD5Util.encode(passwordNew);
        user.setPassword(password2);
        return repository.save(user);
    }

    @Override
    @Transactional
    public User updateInformation(UserUpdateInformationForm form) {

        User user = repository.findOne(form.getId());
        if (user == null) {
            throw new MmallException(ResultEnum.USERNAME_NOT_EXISTS);
        }
        user.setEmail(form.getEmail());
        user.setPhone(form.getPhone());
        user.setQuestion(form.getQuestion());
        user.setAnswer(form.getAnswer());
        return repository.save(user);
    }

    @Override
    public User information(Integer userId) {

        User user = repository.findOne(userId);
        if (user == null) {
            throw new MmallException(ResultEnum.USERNAME_NOT_EXISTS);
        }
        user.setPassword(null);
        return user;
    }

    @Override
    public User adminLogin(String username, String password) {
        User user = repository.findTopByUsername(username);

        if (user == null) {
            throw new MmallException(ResultEnum.USERNAME_NOT_EXISTS);
        }

        String md5Password = getPasswordMD5(password);

        if (!user.getPassword().equals(md5Password)) {
            throw new MmallException(ResultEnum.PASSWORD_ERROR);
        }
        if(!user.getRole().equals(RoleEnum.ADMIN.getCode())){
            throw new MmallException(ResultEnum.USERNAME_ROLE_ERROR);
        }
        return user;
    }

    private String getPasswordMD5(String password){
        String md5Password = MD5Util.encode(password+customConfig.getUserPasswordSalt());
        return md5Password;
    }
}
