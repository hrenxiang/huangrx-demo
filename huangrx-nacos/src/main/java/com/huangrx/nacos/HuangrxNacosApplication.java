package com.huangrx.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 开启nacos注解
 *
 * @author    hrenxiang
 * @since    2022/4/27 8:28 PM
 */
@SpringBootApplication
@EnableDiscoveryClient
public class HuangrxNacosApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuangrxNacosApplication.class, args);
    }

}
