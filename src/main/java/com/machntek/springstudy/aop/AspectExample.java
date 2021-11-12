package com.machntek.springstudy.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public class AspectExample {

    @Before("com.xyz.myapp.CommonPointcuts.dataAccessOperation()")
    public void doAccessCheckDetail() {
        // ...
    }

    @Before("execution(* com.machntek.springstudy.dao.*.*(..))")
    public void doAccessCheck() {
        // ...
    }

    @AfterReturning(
            pointcut = "com.machntek.springstudy.CommonPointcuts.dataAccessOperation()",
            returning = "retVal")
    public void doAccessCheck(Object retVal) {
        // ...
    }

    @AfterThrowing(
            pointcut = "com.machntek.springstudy.CommonPointcuts.dataAccessOperation()",
            throwing = "ex")
    public void doRecoveryActions(Exception ex) {
        // ...
    }

    @After("com.machntek.springstudy.CommonPointcuts.dataAccessOperation()")
    public void doReleaseLock() {
        // ...
    }

    @Around("com.machntek.springstudy.CommonPointcuts.businessService()")
    public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
        // start stopwatch
        Object retVal = pjp.proceed();
        // stop stopwatch
        return retVal;
    }

    // 파라미터 패싱 ex1
    @Before("com.xyz.myapp.CommonPointcuts.dataAccessOperation() && args(account,..)")
    public void validateAccount(Account account) {
        // ...
    }

    @Pointcut("com.xyz.myapp.CommonPointcuts.dataAccessOperation() && args(account,..)")
    private void accountDataAccessOperation(Account account) {}

    @Before("accountDataAccessOperation(account)")
    public void validateAccountPointCutSeparate(Account account) {
        // ...
    }
}
