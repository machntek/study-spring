package com.machntek.springstudy.aop.practice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Component
@Aspect
public class TestAspect2 {

    @Pointcut("execution(* com.machntek.springstudy.aop..*Target.*(..))")
    private void pt2() {}

    @Around("pt2()")
    public Object around2(ProceedingJoinPoint pjp) throws Throwable {

        System.out.println(">>>>>>>>> fuck you");
        Object result = pjp.proceed();
        System.out.println(">>>>>>>>> thank you");

        return result;
    }
}
