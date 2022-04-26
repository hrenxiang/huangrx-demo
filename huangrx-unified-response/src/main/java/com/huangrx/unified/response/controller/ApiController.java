package com.huangrx.unified.response.controller;

import com.huangrx.unified.response.domain.BaseResponse;
import com.huangrx.unified.response.domain.ResultCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试 通用返回对象控制器
 *
 * @author hrenxiang
 * @since 2022-04-25 8:06 PM
 */
@RestController
public class ApiController {

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public BaseResponse<String> get() {
        return BaseResponse.success("huangrx， 你真帅！！！");
    }

    @RequestMapping(value = "/getFail", method = RequestMethod.GET)
    public BaseResponse<String> getFail() {
        return BaseResponse.failed(ResultCode.FAILED, "huangrx， 你帅我也不能让你过去！！！");
    }
}
