package com.huangrx.nacos.controller;

import com.huangrx.nacos.module.BasicAuthorizationConfig;
import com.huangrx.nacos.service.ApiServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * test feign + nacos热部署
 *
 * @author hrenxiang
 * @since 2022-04-28 6:48 PM
 */
@RestController
@RequestMapping(value = "/nacos")
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

    /**
     * 测试gateway 和 geteway的熔断降级
     * @return 暴露出去的接口
     */
    @GetMapping("/get2")
    public String get2() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return name;
    }
}
