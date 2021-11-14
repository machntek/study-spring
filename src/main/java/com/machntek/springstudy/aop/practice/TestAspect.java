package com.machntek.springstudy.aop.practice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(3)
@Component
@Aspect
public class TestAspect {

    @Pointcut("execution(* com.machntek.springstudy.aop..*Target.*(..))")
    private void pt() {}

    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        System.out.println(">>>>>>>>> before");
        Object result = pjp.proceed();
        System.out.println(">>>>>>>>> after");

        return result;
    }
}
