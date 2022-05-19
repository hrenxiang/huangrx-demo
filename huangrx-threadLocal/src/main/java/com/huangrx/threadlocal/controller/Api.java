package com.huangrx.threadlocal.controller;

import com.huangrx.threadlocal.config.Container;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 *
 * @author hrenxiang
 * @since 2022-05-19 3:38 PM
 */
@RestController
public class Api {

    @GetMapping("get")
    public String getHead() {

        return Container.threadLocal().get();
    }
}
