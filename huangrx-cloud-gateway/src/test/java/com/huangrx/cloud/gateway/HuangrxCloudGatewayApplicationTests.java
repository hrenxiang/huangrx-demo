package com.huangrx.cloud.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HuangrxCloudGatewayApplicationTests {

    @Value("${123}")
    private String str;

    @Test
    void contextLoads() {
        System.out.println(str);
    }

}
