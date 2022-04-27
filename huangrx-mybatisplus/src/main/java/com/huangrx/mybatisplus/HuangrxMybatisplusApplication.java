package com.huangrx.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ' @MapperScan(value = "com.huangrx.mybatisplus.mapper")
 * 路径配置到 mapper即可，配置为 com.huangrx.mybatisplus.mapper.* 也可以
 *
 * @author    hrenxiang
 * @since    2022/4/27 8:13 PM
 */
@SpringBootApplication
@MapperScan(value = "com.huangrx.mybatisplus.mapper")
public class HuangrxMybatisplusApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuangrxMybatisplusApplication.class, args);
    }

}
