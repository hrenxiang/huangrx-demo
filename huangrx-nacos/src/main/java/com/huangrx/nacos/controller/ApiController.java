package com.huangrx.nacos.controller;

import com.huangrx.nacos.service.ApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * test feign
 *
 * @author hrenxiang
 * @since 2022-04-28 6:48 PM
 */
@RestController
public class ApiController {

    private final ApiServiceImpl apiService;

    public ApiController(ApiServiceImpl apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/get")
    public String get() {
        return apiService.hello();
    }
}
