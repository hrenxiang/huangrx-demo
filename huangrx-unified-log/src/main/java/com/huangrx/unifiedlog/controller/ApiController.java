package com.huangrx.unifiedlog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试 通用返回对象控制器
 *
 * @author hrenxiang
 * @since 2022-04-25 8:06 PM
 */
@Slf4j
@RestController
public class ApiController {

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String get() {
        return "huangrx ----- post";
    }

    @RequestMapping(value = "/get2", method = RequestMethod.GET)
    public String get2() {
        return "huangrx ----- get";
    }
}
