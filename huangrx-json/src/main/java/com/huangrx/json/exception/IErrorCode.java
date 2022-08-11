package com.huangrx.json.exception;

/**
 * 封装API的错误码
 *
 * @author hrenxiang
 * @since 2022-04-24 9:48 PM
 */
public interface IErrorCode {
    /**
     * 获取响应码
     * @return 响应码
     */
    Integer getCode();

    /**
     * 获取错误信息
     * @return 信息
     */
    String getMessage();
}