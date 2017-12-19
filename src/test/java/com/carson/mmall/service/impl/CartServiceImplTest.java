package com.carson.mmall.service.impl;

import com.carson.mmall.VO.CartVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CartServiceImplTest {
    @Autowired
    private CartServiceImpl cartService;
    @Test
    public void cartListTest(){
        CartVO cartVO=cartService.cartList(21);
        log.info("cartVO={}",cartVO);
    }
}