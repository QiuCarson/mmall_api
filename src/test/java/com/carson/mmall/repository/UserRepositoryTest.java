package com.carson.mmall.repository;



import com.carson.mmall.dataobject.User;
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
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void findOneTest(){
        User user=repository.findOne(1);
        log.info(user.toString());
    }

    @Test
    public void findByUsernameTest(){
        String username="123";
        User user=repository.findTopByUsername(username);
        //log.info(user.toString());
    }

}