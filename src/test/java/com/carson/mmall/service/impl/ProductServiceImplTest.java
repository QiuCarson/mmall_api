package com.carson.mmall.service.impl;

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
public class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl productService;

    @Test
    public void listTest() {
        Integer categoryId = 100002;
        String keyword = "";
        Integer pageNum = 0;
        Integer pageSize = 1;
        String orderBy = "default";
        productService.list(categoryId, keyword, pageNum, pageSize, orderBy);
    }
}