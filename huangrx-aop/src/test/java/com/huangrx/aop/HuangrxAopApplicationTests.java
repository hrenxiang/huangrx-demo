package com.huangrx.aop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HuangrxAopApplicationTests {

    @Autowired
    @Qualifier(value = "calculator1")
    private Calculator calculator;

    @Test
    void contextLoads() {

        calculator.add(10,20);

        System.out.println("\n");
    }

}
