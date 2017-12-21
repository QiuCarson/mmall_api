package com.carson.mmall.service;

import com.carson.mmall.VO.OrderCartProductVO;
import com.carson.mmall.VO.OrderPageListVO;
import com.carson.mmall.VO.OrderPageVO;
import com.carson.mmall.VO.OrderVO;

public interface OrderService {
    OrderVO create(Integer userId, Integer shippingId);

    OrderCartProductVO orderCartProduct(Integer userId);

    OrderPageVO list(Integer userId, Integer pageSize, Integer pageNum);

    OrderPageListVO detail(Integer userId, long orderNo);
}
