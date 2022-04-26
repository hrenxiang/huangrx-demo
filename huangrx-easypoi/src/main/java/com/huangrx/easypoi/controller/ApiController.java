package com.huangrx.easypoi.controller;

import com.huangrx.easypoi.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 导入导出控制器
 *
 * @author hrenxiang
 * @since 2022-04-26 3:08 PM
 */
@Slf4j
@RestController
public class ApiController {

    private final ApiService  apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        apiService.export(response);
    }
}
