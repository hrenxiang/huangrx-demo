package com.huangrx.mystruct.po;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Student {
    private Long id;
    private String name;
    private Integer age;
    private Integer sex;
    private LocalDateTime admissionTime;
}