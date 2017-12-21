package com.carson.mmall.converter;

import com.carson.mmall.VO.OrderVO;
import com.carson.mmall.dataobject.Order;
import org.springframework.beans.BeanUtils;

public class Order2OrderVO {
    public OrderVO convert(Order order) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        return orderVO;
    }
}
