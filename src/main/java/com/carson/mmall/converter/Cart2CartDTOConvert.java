package com.carson.mmall.converter;

import com.carson.mmall.dataobject.Cart;
import com.carson.mmall.dto.CartDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class Cart2CartDTOConvert {
    public static CartDTO convert(Cart cart){
        CartDTO cartDTO=new CartDTO();
        BeanUtils.copyProperties(cart,cartDTO);
        return cartDTO;
    }
    public static List<CartDTO> listConvert(List<Cart> cartList){
        return cartList.stream().map(e->convert(e)).collect(Collectors.toList());
    }
}
