package com.huangrx.knife4j.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hrenxiang
 * @version v1.0
 * @since 2022-02-17 2:27 PM
 */
@Data
@ApiModel(value = "地址实体")
public class AddressParam {
    @ApiModelProperty(value = "城市", name = "city", position = 3)
    private String city;
    @ApiModelProperty(value = "邮政编码", name = "zipcode", position = 2)
    private String zipcode;
}
