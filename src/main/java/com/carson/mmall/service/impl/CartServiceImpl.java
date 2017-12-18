package com.carson.mmall.service.impl;

import com.carson.mmall.VO.CartVO;
import com.carson.mmall.dataobject.Cart;
import com.carson.mmall.repository.CartRepository;
import com.carson.mmall.repository.ProductRepository;
import com.carson.mmall.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public CartVO cartList(Integer userId) {
        List<Cart> cartList = cartRepository.findByUserId(userId);


        List<Integer> productIdList=cartList.stream().map(e->e.getProductId()).collect(Collectors.toList());







        return null;
    }
}
