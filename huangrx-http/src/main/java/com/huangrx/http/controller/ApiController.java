package com.huangrx.http.controller;

import com.huangrx.http.feign.HuangrxNacosClient;
import com.huangrx.http.util.HttpUtils;
import com.huangrx.nacos.api.NacosApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试 openfeign
 *
 * @author hrenxiang
 * @since 2022-04-28 6:57 PM
 */
@Slf4j
@RestController
public class ApiController {

    @Autowired
    private HuangrxNacosClient huangrxNacosClient;

    /**
     * 测试 feign
     */
    @GetMapping("/get")
    public void get() {
        String s = huangrxNacosClient.get();
        log.info("结果是：{}", s);
    }

    /**
     * 测试 HttpUtils
     */
    @GetMapping("/get2")
    public void get2() {
        String url = "http://192.168.2.105:28001/get";
        String s = HttpUtils.httpGet(url);
        log.info("结果是：{}", s);
    }

}
