package com.machntek.springstudy.aop;

import org.aspectj.lang.annotation.Pointcut;

public class PointcutExample {
    // execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern)
    //                throws-pattern?)


    // public 메소드 익스큐션
    @Pointcut("execution(public * *(..))")
    private void anyPublicOperation() {}

    @Pointcut("within(com.xyz.myapp.trading..*)")
    private void inTrading() {}

    @Pointcut("anyPublicOperation() && inTrading()")
    private void tradingOperation() {}

    // set으로 시작하는 이름의 메소드
    @Pointcut("execution(* set*(..))")
    public void setterPointcut() {}

    // AccountService 인터페이스에 정의된 메소드들의 익스큐션
    @Pointcut("execution(* com.xyz.service.AccountService.*(..))")
    public void AccountServicePointcut() {}

    // service패키지에 정의된 모든 메소드
    @Pointcut("execution(* com.xyz.service.*.*(..))")
    public void servicePackage() {}

    // service 패키지나 그 하위패키지의 메소드
    @Pointcut("execution(* com.xyz.service..*.*(..))")
    public void serviceAndSubPackage() {}

    // service 패키지 안의 모든 조인포인트(스프링AOP에서는 메소드 execution만임)
    @Pointcut("within(com.xyz.service.*)")
    public void withinServicePackage() {}

    // service 패키지와 하위패키지 안의 조인포인트(스프링AOP에서는 메소드 execution만임)
    @Pointcut("within(com.xyz.service..*)")
    public void withinServiceAndSubPackage() {}

    // AccountService 인터페이스를 구현한 프록시(빈으로 등록됨. 타겟 오브젝트는 빈 아님) 조인포인트(스프링AOP에서는 메소드 execution만임)
    @Pointcut("this(com.xyz.service.AccountService)")
    public void AccountServiceThisPointcut() {}

    // AccountService 인터페이스를 구현한 타겟오브젝트의 조인포인트
    @Pointcut("target(com.xyz.service.AccountService)")
    public void AccountServiceTargetPointcut() {}

    // 단일 매개변수이고 런타임에 전달된 파라미터가 Serializable 인 모든 조인포인트(스프링 AOP에서만 메서드 실행)
    @Pointcut("args(java.io.Serializable)")
    public void serializable() {}

    // 대상 객체에 @Transactional 애노테이션이 있는 모든 조인포인트
    @Pointcut("@target(org.springframework.transaction.annotation.Transactional)")
    public void transactionalTarget() {}

    // 대상 객체의 선언된 타입에 @Transactional 애노테이션이 있는 모든 조인포인트
    @Pointcut("@within(org.springframework.transaction.annotation.Transactional)")
    public void transactionalWithin() {}

    // 실행하는 메소드에 @Transactional 애노테이션이 있는 모든 조인포인트
    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void annotationMethod() {}

    // 단일 매개변수를 사용하고 전달된 파라미터의 런타임 유형에 @Classified 애노테이션이 있는 모든 조인포인트
    @Pointcut("@args(com.xyz.security.Classified)")
    public void classified() {}

    // 빈 이름이 tradeService인 조인포인트
    @Pointcut("bean(tradeService)")
    public void bean() {}

    // 빈 이름이 Service로 끝나는 조인포인트
    @Pointcut("bean(*Service)")
    public void serviceBean() {}
}
