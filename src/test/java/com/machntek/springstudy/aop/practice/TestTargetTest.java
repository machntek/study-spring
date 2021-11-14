package com.machntek.springstudy.aop.practice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestTargetTest {
    @Autowired
    Target tt;

    @Autowired
    DefaultListableBeanFactory dlbf;

    @Test
    public void aspectTest() {
        Target zz = new TestTarget();
        zz.test();
        tt.test();
        System.out.println(tt.getClass().getName());
        System.out.println(zz.getClass().getName());
        String[] beanDefinitionNames = dlbf.getBeanDefinitionNames();
        Object testTarget = dlbf.getBean("testTarget");
        System.out.println(testTarget.getClass());
        System.out.println();
    }
}
