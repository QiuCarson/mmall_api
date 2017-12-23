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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/user")
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
        userService.checkUsername(str, type);
        return ResultVOUtil.success();
    }

    @PostMapping("/get_user_info.do")
    public ResultVO user_info(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);
        if (userId == null) {
            return ResultVOUtil.error(ResultEnum.LONGIN_NOT_AUTH);
        }

        User user = userService.userInfo(userId);
        log.info("username={}", user.toString());
        return ResultVOUtil.success(user);
    }

    @PostMapping("/forget_get_question.do")
    public ResultVO forget_get_question(@RequestParam("username") String username) {
        String question = userService.forgetGetQuestion(username);
        return ResultVOUtil.success(question);
    }

    @PostMapping("/forget_check_answer.do")
    public ResultVO forget_check_answer(@RequestParam("username") String username,
                                        @RequestParam("question") String question,
                                        @RequestParam("answer") String answer) {
        String token = userService.forgetCheckAnswer(username, question, answer);
        return ResultVOUtil.success(token);
    }

    @PostMapping("/forget_reset_password.do")
    public ResultVO forget_reset_password(@RequestParam("username") String username,
                                          @RequestParam("passwordNew") String passwordNew,
                                          @RequestParam("forgetToken") String forgetToken) {
        User user = userService.forgetResetPassword(username, passwordNew, forgetToken);
        return ResultVOUtil.success();
    }

    @PostMapping("/reset_password.do")
    public ResultVO reset_password(@RequestParam("passwordOld") String passwordOld,
                                   @RequestParam("passwordNew") String passwordNew, HttpSession session) {
        String username = session.getAttribute("username").toString();
        User user = userService.resetPassword(username, passwordOld, passwordNew);
        return ResultVOUtil.success();
    }

    @PostMapping(value = "/update_information.do")
    public ResultVO authUpdateInformation(@Valid UserUpdateInformationForm form, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            throw new MmallException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }

        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);
        form.setId(userId);
        User user = userService.updateInformation(form);
        return ResultVOUtil.success();
    }


    @PostMapping("/get_information.do")
    public ResultVO authGetInformation(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);
        User user = userService.information(userId);
        return ResultVOUtil.success(user);
    }

    @PostMapping("/logout.do")
    public ResultVO logout(HttpSession session) {
        session.removeAttribute(Const.SESSION_AUTH);
        session.removeAttribute(Const.SESSION_AUTH_ID);
        return ResultVOUtil.success();
    }

}
