package com.huangrx.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.huangrx.mybatisplus.mapper")
public class HuangrxMybatisplusApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuangrxMybatisplusApplication.class, args);
    }

}
