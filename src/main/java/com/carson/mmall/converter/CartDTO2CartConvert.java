package com.carson.mmall.converter;

import com.carson.mmall.dataobject.Cart;
import com.carson.mmall.dto.CartDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class CartDTO2CartConvert {
    public static Cart convert(CartDTO cartDTO){
        Cart cart=new Cart();
        BeanUtils.copyProperties(cartDTO,cart);
        return cart;
    }
    public static List<Cart> listConvert(List<CartDTO> cartDTOList){
        return cartDTOList.stream().map(e->convert(e)).collect(Collectors.toList());
    }
}
