package com.carson.mmall.service;

import com.carson.mmall.VO.OrderCartProductVO;
import com.carson.mmall.VO.OrderPageVO;
import com.carson.mmall.dto.OrderDTO;

import javax.servlet.http.HttpServletRequest;

public interface OrderService {
    OrderDTO create(Integer userId, Integer shippingId);

    OrderCartProductVO orderCartProduct(Integer userId);

    OrderPageVO list(Integer userId, Integer pageSize, Integer pageNum);

    OrderDTO detail(Integer userId, long orderNo);

    OrderDTO cancel(Integer userId, long orderNo);

    String pay(Integer userId, long orderNo);

    Boolean queryOrderPayStatus(Integer userId, long orderNo);

    OrderPageVO adminList(Integer pageSize, Integer pageNum);

    OrderPageVO adminSearch(Long orderNo, Integer pageSize, Integer pageNum);

    OrderDTO adminDetail(Long orderNo);

    OrderDTO adminSendGoods(Long orderNo);

    String alipayCallback(HttpServletRequest request);
}
