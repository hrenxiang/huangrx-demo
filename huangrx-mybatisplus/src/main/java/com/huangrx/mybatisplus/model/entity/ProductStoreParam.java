package com.huangrx.mybatisplus.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProductStoreParam{


    private List<Integer> storeIds;
    private Integer productId;
    private String productName;
    private String productSn;
    private String productNbsn;
    private Integer fastProductFlag;

}