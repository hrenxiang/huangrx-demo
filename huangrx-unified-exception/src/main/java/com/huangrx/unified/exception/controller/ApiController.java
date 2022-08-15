package com.huangrx.unified.exception.controller;

import com.huangrx.unified.exception.domain.BaseResponse;
import com.huangrx.unified.exception.domain.ResultCode;
import com.huangrx.unified.exception.exception.Asserts;
import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
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

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public BaseResponse<String> get(@RequestBody @Validated Person person) {
        return BaseResponse.success(person.getName() + "， 你真帅！！！");
    }

    @RequestMapping(value = "/getFail", method = RequestMethod.GET)
    public BaseResponse<String> getFail() {
        Asserts.isTrue("1".equals("2"), "不相等");
        //throw new IndexOutOfBoundsException();
        return BaseResponse.failed(ResultCode.FAILED, "huangrx， 你帅我也不能让你过去！！！");
    }
}
