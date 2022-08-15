package com.huangrx.mystruct.dto;

import lombok.Data;
import java.io.Serializable;
@Data
public class OrderDTO implements Serializable {
    private Long orderId;
    private String buyerPhone;
    private String buyerAddress;
    private Long amount;
    private Integer payStatus;
    private String orderTime;
}