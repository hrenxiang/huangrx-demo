package com.huangrx.evenlistener;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class HuangrxEvenlistenerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuangrxEvenlistenerApplication.class, args);
    }

}
