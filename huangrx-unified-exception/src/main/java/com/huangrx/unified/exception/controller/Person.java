package com.huangrx.unified.exception.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 实体
 *
 * @author hrenxiang
 * @since 2022-04-25 9:09 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @NotBlank(message = "名字不能为空")
    private String name;
}
