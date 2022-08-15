package com.huangrx.mystruct.po;

import lombok.Data;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@Builder
public class Order implements Serializable {
    private Long id;
    private String buyerPhone;
    private String buyerAddress;
    private Long amount;
    private Integer payStatus;
    private LocalDateTime createTime;
}