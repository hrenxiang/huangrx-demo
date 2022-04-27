package com.huangrx.logstash.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 输出日志
 *
 * @author hrenxiang
 * @since 2022-04-27 10:28 AM
 */
@RestController
@Slf4j
public class ApiLogMain {

    @GetMapping("/get")
    public void get() {
        log.info("huangrx----- 帅帅帅！！！");
    }
}
