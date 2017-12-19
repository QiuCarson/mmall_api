package com.carson.mmall.service;

import com.carson.mmall.VO.CartVO;

import java.util.List;

public interface CartService {
    CartVO cartList(Integer userId);

    CartVO add(Integer userId, Integer productId, Integer quantity);

    CartVO update(Integer userId, Integer productId, Integer quantity);

    CartVO delete(Integer userId, String productIds);

    CartVO select(Integer userId, Integer productId);

    CartVO unselect(Integer userId, Integer productId);

    Integer count(Integer userId);

    CartVO selectAll(Integer userId);

    CartVO unSelectAll(Integer userId);
}
