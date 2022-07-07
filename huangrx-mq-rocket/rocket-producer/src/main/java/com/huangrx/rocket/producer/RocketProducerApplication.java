package com.huangrx.rocket.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.huangrx.rocket.producer.*")
@SpringBootApplication
public class RocketProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RocketProducerApplication.class, args);
    }

}
