package com.huangrx.cloud.gateway.exception;

import com.huangrx.cloud.gateway.domain.response.IErrorCode;

import java.io.Serializable;

/**
 * 自定义API异常
 *
 * @author hrenxiang
 * @since 2022-04-24 9:48 PM
 */
public class ApiException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 5900643350419175492L;
    private IErrorCode errorCode;
    private String message;

    public ApiException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ApiException(String message) {
        super(message);
        this.message = message;
    }

    public ApiException(String message, IErrorCode errorCode) {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }

    public ApiException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public ApiException(String message, IErrorCode errorCode, Throwable e) {
        super(message, e);
        this.message = message;
        this.errorCode = errorCode;
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(IErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}