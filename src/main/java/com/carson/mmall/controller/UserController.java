package com.carson.mmall.controller;

import com.carson.mmall.VO.ResultVO;
import com.carson.mmall.common.Const;
import com.carson.mmall.dataobject.User;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.exception.MmallException;
import com.carson.mmall.form.UserForm;
import com.carson.mmall.form.UserUpdateInformationForm;
import com.carson.mmall.service.impl.UserServiceImpl;
import com.carson.mmall.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/login.do")
    public ResultVO login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {

        User user = userService.login(username, password);
        session.setAttribute(Const.SESSION_AUTH, user.getUsername());
        session.setAttribute(Const.SESSION_AUTH_ID, user.getId());

        return ResultVOUtil.success(user);
    }


    @PostMapping("/register.do")
    public ResultVO register(@Valid UserForm form, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            throw new MmallException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }

        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            throw new MmallException(ResultEnum.PASSWORD_NOT_EQUALS_ERROR);
        }
        User user = userService.register(form);
        session.setAttribute(Const.SESSION_AUTH, user.getUsername());
        session.setAttribute(Const.SESSION_AUTH_ID, user.getId());
        return ResultVOUtil.success();
    }


    @PostMapping("/check_valid.do")
    public ResultVO check_username(@RequestParam("str") String str, @RequestParam("type") String type) {
        userService.check_username(str, type);
        return ResultVOUtil.success();
    }

    @PostMapping("/get_user_info.do")
    public ResultVO user_info(HttpSession session) {
        String username = (String) session.getAttribute(Const.SESSION_AUTH);
        if (username == null) {
            return ResultVOUtil.error(ResultEnum.LONGIN_NOT_AUTH);
        }

        User user = userService.user_info(username);
        log.info("username={}", user.toString());
        return ResultVOUtil.success(user);
    }

    @PostMapping("/forget_get_question.do")
    public ResultVO forget_get_question(@RequestParam("username") String username) {
        String question = userService.forget_get_question(username);
        return ResultVOUtil.success(question);
    }

    @PostMapping("/forget_check_answer.do")
    public ResultVO forget_check_answer(@RequestParam("username") String username,
                                        @RequestParam("question") String question,
                                        @RequestParam("answer") String answer) {
        String token = userService.forget_check_answer(username, question, answer);
        return ResultVOUtil.success(token);
    }

    @PostMapping("/forget_reset_password.do")
    public ResultVO forget_reset_password(@RequestParam("username") String username,
                                          @RequestParam("passwordNew") String passwordNew,
                                          @RequestParam("forgetToken") String forgetToken) {
        User user = userService.forget_reset_password(username, passwordNew, forgetToken);
        return ResultVOUtil.success();
    }

    @PostMapping("/reset_password.do")
    public ResultVO reset_password(@RequestParam("passwordOld") String passwordOld,
                                   @RequestParam("passwordNew") String passwordNew, HttpSession session) {
        String username = session.getAttribute("username").toString();
        User user = userService.reset_password(username, passwordOld, passwordNew);
        return ResultVOUtil.success();
    }

    @PostMapping(value = "/update_information.do")
    public ResultVO authUpdateInformation(@Valid UserUpdateInformationForm form, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            throw new MmallException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }

        String username = (String) session.getAttribute(Const.SESSION_AUTH);
        form.setUsername(username);
        User user = userService.update_information(form);
        return ResultVOUtil.success();
    }


    @PostMapping("/get_information.do")
    public ResultVO authGetInformation(HttpSession session) {
        String username = (String) session.getAttribute(Const.SESSION_AUTH);
        User user = userService.information(username);
        return ResultVOUtil.success(user);
    }

    @PostMapping("/logout.do")
    public ResultVO logout(HttpSession session) {
        session.removeAttribute(Const.SESSION_AUTH);
        session.removeAttribute(Const.SESSION_AUTH_ID);
        return ResultVOUtil.success();
    }

}
