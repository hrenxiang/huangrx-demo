package com.huangrx.aop;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class HuangrxAopApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(HuangrxAopApplication.class);
        CalculatorPureImpl bean = context.getBean(CalculatorPureImpl.class);
        bean.add(2,3);
    }

}
