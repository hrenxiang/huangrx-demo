package com.huangrx.nacos;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@Slf4j
@SpringBootTest
class HuangrxNacosApplicationTests {

    //@Value("${demo.name}")
    //String name;

    @Test
    void contextLoads() {
       // log.info("注入结果：{}", name);

        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("/Users/hrenxiang/downloads/huangrx/123.jpg"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
