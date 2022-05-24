package com.huangrx.log;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class HuangrxLogApplicationTests {

    @Test
    void contextLoads() {
        log.info("================>> INFO");
        log.error("================>> ERROR");
        log.warn("================>> WARN");
    }

}
