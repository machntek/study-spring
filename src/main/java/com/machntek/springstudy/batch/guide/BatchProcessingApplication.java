package com.machntek.springstudy.batch.guide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatchProcessingApplication {

    public static void main(String[] args) {
        // job이 끝나면 JVM을 exit하게 해준다.
        System.exit(SpringApplication.exit(SpringApplication.run(BatchProcessingApplication.class, args)));
    }
}
