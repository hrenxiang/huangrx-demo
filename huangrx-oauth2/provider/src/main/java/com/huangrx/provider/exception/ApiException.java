package com.huangrx.provider.exception;

import com.huangrx.provider.domain.response.IErrorCode;

/**
 * 自定义API异常
 *
 * @author hrenxiang
 * @since 2022-04-24 9:48 PM
 */
public class ApiException extends RuntimeException {

    private IErrorCode errorCode;

    public ApiException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}
