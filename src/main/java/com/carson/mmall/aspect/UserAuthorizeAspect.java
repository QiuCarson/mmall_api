package com.carson.mmall.aspect;

import com.carson.mmall.common.Const;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.exception.MmallException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Aspect
@Component
@Slf4j
public class UserAuthorizeAspect {


    @Pointcut("execution(public * com.carson.mmall.controller.Cart*.*(..)) && !execution(public * com.carson.mmall.controller.Cart*.count(..)) " +
            "|| execution(public * com.carson.mmall.controller.Order*.*(..)) " +
            "|| execution(public * com.carson.mmall.controller.Shipping*.*(..)) " +
            "|| execution(public * com.carson.mmall.controller.User*.auth*(..))")
    public void verify() {
    }




    @Before("verify()")
    public void doVerify() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Integer userId = (Integer) request.getSession().getAttribute(Const.SESSION_AUTH_ID);
        if (userId == null || userId < 1) {
            throw new MmallException(ResultEnum.USERNAME_NOT_AUTH);
        }
    }


}
