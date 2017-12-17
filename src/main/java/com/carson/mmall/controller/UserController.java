package com.carson.mmall.controller;

import com.carson.mmall.VO.ResultVO;
import com.carson.mmall.dataobject.User;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.exception.MmallException;
import com.carson.mmall.form.UserForm;
import com.carson.mmall.service.impl.UserServiceImpl;
import com.carson.mmall.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/login.do")
    public ResultVO login(@RequestParam("username") String username,@RequestParam("password") String password){

        User user=userService.login(username,password);
        return ResultVOUtil.success(user);
    }


    @PostMapping("/register.do")
    public ResultVO register(@Valid UserForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new MmallException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }

        if(!form.getPassword().equals(form.getPasswordConfirm())){
            throw new MmallException(ResultEnum.PASSWORD_NOT_EQUALS_ERROR);
        }
        User user=userService.register(form);
        return ResultVOUtil.success();
    }


    @PostMapping("/check_valid.do")
    public ResultVO check_username(@RequestParam("str") String str,@RequestParam("type") String type){
        userService.check_username(str,type);
        return ResultVOUtil.success();
    }
}
