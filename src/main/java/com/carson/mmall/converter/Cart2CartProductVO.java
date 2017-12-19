package com.carson.mmall.converter;

import com.carson.mmall.VO.CartProductVO;
import com.carson.mmall.dataobject.Cart;

import java.util.List;
import java.util.stream.Collectors;

public class Cart2CartProductVO {

    public static CartProductVO convert(Cart cart){
        CartProductVO cartProductVO=new CartProductVO();
        cartProductVO.setQuantity(cart.getQuantity());
        cartProductVO.setProductId(cart.getProductId());
        cartProductVO.setId(cart.getId());
        cartProductVO.setProductChecked(cart.getChecked());
        return cartProductVO;
    }

    public static List<CartProductVO> listConvert(List<Cart> cartList){
        return cartList.stream().map(e->convert(e)).collect(Collectors.toList());
    }
}
