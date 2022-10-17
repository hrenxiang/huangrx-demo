package com.huangrx.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 *
 * @author hrenxiang
 * @since 2022-10-14 17:05:38
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties
public class HuangrxCloudGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuangrxCloudGatewayApplication.class, args);
    }

}