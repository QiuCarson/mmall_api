package com.carson.mmall.controller;

import com.carson.mmall.VO.*;
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

    @GetMapping("/list.do")
    public ResultVO list(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                         HttpSession session) {
        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);
        OrderPageVO orderPageVO = orderService.list(userId, pageSize, pageNum);
        return ResultVOUtil.success(orderPageVO);
    }
    @GetMapping("/detail.do")
    public ResultVO detail(@RequestParam("orderNo") Long orderNo,HttpSession session){
        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);
        OrderPageListVO orderPageListVO= orderService.detail(userId,orderNo);
        return ResultVOUtil.success(orderPageListVO);
    }
}
