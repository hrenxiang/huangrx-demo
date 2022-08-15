package com.huangrx.mystruct.po;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class GoodType {
    private Long id;
    private String name;
    private int show;
    private int order;
}