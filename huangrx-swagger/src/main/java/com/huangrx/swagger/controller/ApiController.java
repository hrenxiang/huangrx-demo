package com.huangrx.swagger.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制器
 *
 * swagger 访问地址 ：<a href="http://localhost:8087/swagger-ui/index.html">http://localhost:8087/swagger-ui/index.html</a>
 *
 * @author hrenxiang
 * @since 2022-04-25 5:08 PM
 */
@Api(tags = "APi控制器")
@RestController
public class ApiController {

    @ApiOperation("获取夸奖")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", required = true)
    })
    @RequestMapping(value = "/get",method=RequestMethod.GET)
    public String get(@RequestParam("name") String name) {
        return name + ", 你真的很不错呀！！！";
    }
}
