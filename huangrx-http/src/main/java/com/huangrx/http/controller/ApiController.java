package com.huangrx.http.controller;

import com.huangrx.http.feign.HuangrxNacosClient;
import com.huangrx.http.util.HttpUtils;
import com.huangrx.nacos.api.NacosApi;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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

    private final OkHttpClient okHttpClient = new OkHttpClient();

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

    /**
     * 测试 okhttp
     */
    @GetMapping("/get3")
    public void get3() {
        Request build = new Request.Builder()
                .url("http://192.168.2.103:28001/get")
                .build();

        try {
            Response execute = okHttpClient.newCall(build).execute();
            if (!execute.isSuccessful()) {
                log.info("msg: {}", "Unexpected code " + execute);
            }

            log.info("execute: {}", execute);
            log.info("Server: {}", execute.header("Server"));
            log.info("Date: {}", execute.header("Date"));
            log.info("Vary: {}", execute.header("Vary"));

        } catch (IOException e) {
            log.error("msg: {}", e.getMessage());
        }

    }

}
