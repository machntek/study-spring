package com.machntek.springstudy.aop.practice;

import org.springframework.stereotype.Service;

@Service
public class TestTarget implements Target {

    public String test() {
        return "fuck you";
    }
}
