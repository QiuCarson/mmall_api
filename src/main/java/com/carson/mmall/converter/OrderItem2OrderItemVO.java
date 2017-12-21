package com.carson.mmall.converter;

import com.carson.mmall.VO.OrderItemVO;
import com.carson.mmall.dataobject.OrderItem;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItem2OrderItemVO {
    public static OrderItemVO convert(OrderItem orderItem){
        OrderItemVO orderItemVO=new OrderItemVO();
        BeanUtils.copyProperties(orderItem,orderItemVO);
        return orderItemVO;
    }

    public static List<OrderItemVO> listConvert(List<OrderItem> orderItemList){
        return orderItemList.stream().map(e->convert(e)).collect(Collectors.toList());
    }
}
