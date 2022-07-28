package com.huangrx.evenlistener;

import com.huangrx.evenlistener.translation.service.ApiService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class HuangrxEvenlistenerApplicationTests {

    @Resource
    ApiService apiService;

    @Test
    void testTranslationEvent() {
        apiService.update("xx");
    }

    @Test
    void testAsync() {
        apiService.testAsync();
    }

    @Test
    void contextLoads() {
    }

}
