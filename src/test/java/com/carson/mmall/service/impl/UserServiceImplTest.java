package com.carson.mmall.service.impl;

import com.carson.mmall.dataobject.User;
import com.carson.mmall.repository.UserRepository;
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
public class UserServiceImplTest {
    @Autowired
    private UserRepository repository;

    @Autowired
    private UserServiceImpl userService;

    @Test
    public void loginTest(){

        User user=userService.login("admin","123456");
        log.info("测试结束");
    }

    @Test
    public void redisTest(){
        User user=userService.login("admin","123456");
        log.info("测试结束");
    }
}