package com.huangrx.i18n.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author        hrenxiang
 * @since         2022-08-17 11-44-54
 */
@Data
@Accessors(chain = true)
public class Response<T> {
    private int code;
    private String msg;
    private T data;
}