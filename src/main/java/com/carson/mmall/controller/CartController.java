package com.carson.mmall.controller;

import com.carson.mmall.VO.CartVO;
import com.carson.mmall.VO.ResultVO;
import com.carson.mmall.common.Const;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.exception.MmallException;
import com.carson.mmall.service.CartService;
import com.carson.mmall.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/list.do")
    public ResultVO list(HttpSession session){
        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);
        String username=(String) session.getAttribute(Const.SESSION_AUTH);
        if(username==null || username.isEmpty()){
            throw new MmallException(ResultEnum.USERNAME_NOT_AUTH);
        }
        CartVO cartVO=cartService.cartList(userId);
        return ResultVOUtil.success();
    }
}
