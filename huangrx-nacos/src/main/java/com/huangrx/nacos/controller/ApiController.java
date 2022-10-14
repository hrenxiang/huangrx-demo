package com.huangrx.nacos.controller;

import com.huangrx.nacos.module.BasicAuthorizationConfig;
import com.huangrx.nacos.service.ApiServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * test feign + nacos热部署
 *
 * @author hrenxiang
 * @since 2022-04-28 6:48 PM
 */
@RestController
public class ApiController {

    @Value("${huangrx}")
    private String name;

    private final ApiServiceImpl apiService;

    private final BasicAuthorizationConfig basicAuthorizationConfig;

    public ApiController(ApiServiceImpl apiService, BasicAuthorizationConfig basicAuthorizationConfig) {
        this.apiService = apiService;
        this.basicAuthorizationConfig = basicAuthorizationConfig;
    }

    @GetMapping("/get1")
    public String get1() {
        return apiService.hello();
    }

    /**
     * 测试feign
     * @return 暴露出去的接口
     */
    @GetMapping("/get")
    public String get() {
        return name;
    }
}
