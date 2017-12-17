package com.carson.mmall.controller;

import com.carson.mmall.VO.ResultVO;
import com.carson.mmall.dataobject.User;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.exception.MmallException;
import com.carson.mmall.form.UserForm;
import com.carson.mmall.service.impl.UserServiceImpl;
import com.carson.mmall.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/login.do")
    public ResultVO login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session){

        User user=userService.login(username,password);
        session.setAttribute("user",user.getUsername());
        return ResultVOUtil.success(user);
    }


    @PostMapping("/register.do")
    public ResultVO register(@Valid UserForm form, BindingResult bindingResult, HttpSession session){
        if(bindingResult.hasErrors()){
            throw new MmallException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }

        if(!form.getPassword().equals(form.getPasswordConfirm())){
            throw new MmallException(ResultEnum.PASSWORD_NOT_EQUALS_ERROR);
        }
        User user=userService.register(form);
        session.setAttribute("user",user.getUsername());
        return ResultVOUtil.success();
    }


    @PostMapping("/check_valid.do")
    public ResultVO check_username(@RequestParam("str") String str,@RequestParam("type") String type){
        userService.check_username(str,type);
        return ResultVOUtil.success();
    }

    @PostMapping("/get_user_info.do")
    @Cacheable(cacheNames = "user",key = "123")
    public ResultVO user_info( HttpSession session){
        String username=session.getAttribute("user").toString();
       //String username="admin1";
        User user=userService.user_info(username);
        log.info("username={}",user.toString());
        return ResultVOUtil.success(user);
    }
}
