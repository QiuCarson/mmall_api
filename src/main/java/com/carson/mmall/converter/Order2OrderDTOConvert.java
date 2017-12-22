package com.carson.mmall.converter;

import com.carson.mmall.dataobject.Cart;
import com.carson.mmall.dataobject.Order;
import com.carson.mmall.dto.CartDTO;
import com.carson.mmall.dto.OrderDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class Order2OrderDTOConvert {
    public static OrderDTO convert(Order order){
        OrderDTO orderDTO=new OrderDTO();
        BeanUtils.copyProperties(order,orderDTO);
        return orderDTO;
    }
    public static List<OrderDTO> listConvert(List<Order> orderList){
        return orderList.stream().map(e->convert(e)).collect(Collectors.toList());
    }
}
