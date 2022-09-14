package com.huangrx.huangrx.redis.domain;

import lombok.Getter;

/**
 * 枚举了一些常用API操作码
 *
 * @author hrenxiang
 * @since 2022-04-24 9:48 PM
 */
public enum ResultCode implements IErrorCode {
    /**
     * 操作成功
     */
    SUCCESS(0, "操作成功"),
    /**
     * 操作失败
     */
    FAILED(500, "操作失败"),
    /**
     * 参数检验失败
     */
    VALIDATE_FAILED(506, "参数检验失败"),
    /**
     * 暂未登录或token已经过期
     */
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    /**
     * 没有相关权限
     */
    FORBIDDEN(403, "没有相关权限"),
    /**
     * 服务器错误
     */
    SERVER_ERROR(500, "服务器错误"),
    /**
     * 系统错误
     */
    ADMIN_ERROR(406, "The system is abnormal. Contact the administrator"),
    /**
     * Parameter format is abnormal!
     */
    PARAMS_FAILED(402, "Parameter format is abnormal!");

    @Getter
    private final long code;
    @Getter
    private final String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }
}
