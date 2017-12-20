package com.carson.mmall.service;

import com.carson.mmall.VO.OrderCartProductVO;
import com.carson.mmall.VO.OrderVO;

public interface OrderService {
    OrderVO create(Integer userId, Integer shippingId);
    OrderCartProductVO orderCartProduct(Integer userId);
}
