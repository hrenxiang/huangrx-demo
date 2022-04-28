package com.huangrx.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author hrenxiang
 * @since 2022-04-28 11:37 AM
 */
@SpringBootApplication
public class RabbitmqConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitmqConsumerApplication.class, args);
    }
}
