package com.carson.mmall.controller;

import com.carson.mmall.VO.OrderCartProductVO;
import com.carson.mmall.VO.OrderVO;
import com.carson.mmall.VO.ResultVO;
import com.carson.mmall.common.Const;
import com.carson.mmall.service.OrderService;
import com.carson.mmall.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @GetMapping("/create.do")
    public ResultVO create(@RequestParam("shippingId") Integer shippingId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);
        OrderVO orderVO = orderService.create(userId, shippingId);
        return ResultVOUtil.success(orderVO);
    }

    @GetMapping("/get_order_cart_product.do")
    public ResultVO orderCartProduct(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);
        OrderCartProductVO orderCartProductVO = orderService.orderCartProduct(userId);
        return ResultVOUtil.success(orderCartProductVO);
    }
}
