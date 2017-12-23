package com.carson.mmall.service.impl;

import com.carson.mmall.enums.PaymentTypeEnum;
import com.carson.mmall.utils.EnumUtil;
import com.carson.mmall.utils.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {
    @Autowired
    private OrderServiceImpl orderService;

    @Test
    public void create() {
        Integer userId=23;
        Integer shipping=31;
        orderService.create(userId,shipping);
    }

    @Test
    public void list(){

        //getEnumMessage(PaymentTypeEnum.class,1);
        PaymentTypeEnum paymentTypeEnum= EnumUtil.getByCode(PaymentTypeEnum.class,1);
        log.info("paymentTypeEnum={}",paymentTypeEnum.getMessage());
    }

    @Test
    public void pageTest(){
    }

}