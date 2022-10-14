package com.huangrx.knife4j.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.huangrx.knife4j.model.enums.ResponseStatus;
import com.huangrx.knife4j.model.vo.AddressParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Address controller test demo.
 *
 * @author pdai
 */
@Api(value = "Address Interfaces", tags = "地址模块")
//@ApiSort(2)
@ApiSupport(order = 2)
@RestController
@RequestMapping("/address")
public class AddressController {
    /**
     * <a href="http://localhost:8080/address/add">http://localhost:8080/address/add</a> .
     *
     * swaggerModel 里 只有添加了 @RequestBody 才会显现 或者使用
     *
     * new Docket(DocumentationType.SWAGGER_2)
     *                 .build()
     *                 .additionalModels(typeResolver.resolve(SmartSourceBaseConfig.class))
     *                 .additionalModels(typeResolver.resolve(PlatformResponse.class))
     *                 .additionalModels(typeResolver.resolve(OrganizationResponse.class));
     *
     * @param addressParam param
     * @return address
     */
    @ApiOperation("01 - Add Address")
    @ApiOperationSupport(order = 1)
    @PostMapping("add")
    public ResponseEntity<String> add(@RequestBody AddressParam addressParam) {
        int i = 1/0;
        ResponseEntity.status(500);
        return ResponseEntity.ok(ResponseStatus.HTTP_STATUS_403.getResponseCode());
    }

    @ApiOperation("02 - test Address")
    @ApiOperationSupport(order = 2)
    @PostMapping("test1")
    public ResponseEntity<String> testSort1(@RequestBody AddressParam addressParam) {
        return ResponseEntity.ok(ResponseStatus.HTTP_STATUS_403.getResponseCode());
    }

    @ApiOperation("03 - test Address")
    @ApiOperationSupport(order = 3)
    @PostMapping("test2")
    public ResponseEntity<String> testSort2(@RequestBody AddressParam addressParam) {
        return ResponseEntity.ok(ResponseStatus.HTTP_STATUS_403.getResponseCode());
    }

}
