package com.huangrx.unified.response.domain;

import lombok.Getter;

/**
 * 枚举了一些常用API操作码
 *
 * @author hrenxiang
 * @since 2022-04-24 9:48 PM
 */
public enum ResultCode implements IErrorCode {
    SUCCESS(0, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(506, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限");

    @Getter
    private long code;
    @Getter
    private String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

}
