package com.carson.mmall.controller.admin;

import com.carson.mmall.VO.ResultVO;
import com.carson.mmall.common.Const;
import com.carson.mmall.dataobject.User;
import com.carson.mmall.service.UserService;
import com.carson.mmall.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/user")
public class AdminUserController {

    @Autowired
    private UserService userService;
    @PostMapping("/login.do")
    public ResultVO login(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          HttpSession session){
        User user=userService.adminLogin(username,password);
        session.setAttribute(Const.SESSION_AUTH_ID,user.getId());
        return ResultVOUtil.success(user);
    }
}
