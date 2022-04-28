package com.huangrx.nacos.service;

import org.springframework.stereotype.Service;

/**
 * 测试 feign 模块，作为提供者
 *
 * @author hrenxiang
 * @since 2022-04-28 6:19 PM
 */
@Service
public class ApiServiceImpl {

    public String hello() {
        return "huangrx ----- you are beautiful!!!";
    }
}
