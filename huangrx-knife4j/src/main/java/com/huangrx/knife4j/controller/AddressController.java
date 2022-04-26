package com.huangrx.knife4j.controller;

import com.huangrx.knife4j.model.enums.ResponseStatus;
import com.huangrx.knife4j.model.vo.AddressParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Address controller test demo.
 *
 * @author pdai
 */
@Api(value = "Address Interfaces", tags = "地址模块")
@RestController
@RequestMapping("/address")
public class AddressController {
    /**
     * <a href="http://localhost:8080/address/add">http://localhost:8080/address/add</a> .
     *
     * @param addressParam param
     * @return address
     */
    @ApiOperation("Add Address")
    @PostMapping("add")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "city", type = "query", dataTypeClass = String.class, required = true),
            @ApiImplicitParam(name = "zipcode", type = "query", dataTypeClass = String.class, required = true)
    })
    public ResponseEntity<String> add(AddressParam addressParam) {
        int i = 1/0;
        ResponseEntity.status(500);
        return ResponseEntity.ok(ResponseStatus.HTTP_STATUS_403.getResponseCode());
    }

}
