package com.huangrx.mystruct.po;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class GoodInfo {
    private Long id;
    private String title;
    private double price;
    private int order;
    private Long typeId;
}