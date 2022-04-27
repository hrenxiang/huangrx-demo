package com.huangrx.nacos;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class HuangrxNacosApplicationTests {

    @Value("${demo.name}")
    String name;

    @Test
    void contextLoads() {
        log.info("注入结果：{}", name);
    }

}
