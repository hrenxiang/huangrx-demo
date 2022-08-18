package com.huangrx;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hrenxiang
 * @since 2022-08-18 14:43
 */
@SpringBootApplication
@EnableDubbo
public class HuangrxDubboProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(HuangrxDubboProviderApplication.class);
    }
}
