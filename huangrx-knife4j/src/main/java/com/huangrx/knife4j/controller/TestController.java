package com.huangrx.knife4j.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.huangrx.knife4j.model.enums.ResponseStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hrenxiang
 * @since 2022-08-24 15:31
 */
@Api(value = "TEST Interfaces", tags = "测试模块")
@ApiSort(1)
@RestController
@RequestMapping("/test")
public class TestController {

    @ApiOperation("test")
    @PostMapping("test")
    public ResponseEntity<String> test() {
        int i = 1/0;
        ResponseEntity.status(500);
        return ResponseEntity.ok(ResponseStatus.HTTP_STATUS_403.getResponseCode());
    }
}
