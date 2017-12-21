package com.carson.mmall.service.impl;

import com.carson.mmall.enums.PaymentTypeEnum;
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

        PaymentTypeEnum result=getByIntegerTypeCode(PaymentTypeEnum.class,"getCode",1);
        log.info("dd={}",result.getMessage());
    }


    public static <T extends Enum<T>> T getByIntegerTypeCode(Class<T> clazz,String getTypeCodeMethodName, Integer typeCode){
        T result = null;
        try{
            T[] arr = clazz.getEnumConstants();
            Method targetMethod = clazz.getDeclaredMethod(getTypeCodeMethodName);
            Integer typeCodeVal = null;
            for(T entity:arr){
                typeCodeVal = Integer.valueOf(targetMethod.invoke(entity).toString());
                if(typeCodeVal.equals(typeCode)){
                    result = entity;
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return result;
    }
}