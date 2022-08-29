package com.huangrx.huangrx.redis.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;

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
    private int[] indices;
    private int usedCount;
    private String message;
    private transient Throwable throwable;

    public ApiException(Throwable cause) {
        this.throwable = cause;
        this.message = ExceptionUtils.getStackTrace(throwable);
    }

    public ApiException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ApiException(String message) {
        super(message);
    }


    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiException(String format, Object... arguments) {
        init(format, arguments);
        fillInStackTrace();
        this.message = formatMessage(format, arguments);
        if (throwable != null) {
            this.message += System.lineSeparator() + ExceptionUtils.getStackTrace(throwable);
        }
    }

    private void init(String format, Object... arguments) {
        // divide by 2
        final int len = Math.max(1, format == null ? 0 : format.length() >> 1);
        // LOG4J2-1542 ensure non-zero array length
        this.indices = new int[len];
        final int placeholders = ParameterFormatter.countArgumentPlaceholders2(format, indices);
        initThrowable(arguments, placeholders);
        this.usedCount = Math.min(placeholders, arguments == null ? 0 : arguments.length);
    }

    private void initThrowable(final Object[] params, final int usedParams) {
        if (params != null) {
            final int argCount = params.length;
            if (usedParams < argCount && this.throwable == null && params[argCount - 1] instanceof Throwable) {
                this.throwable = (Throwable) params[argCount - 1];
            }
        }
    }

    private String formatMessage(String format, Object... arguments) {
        StringBuilder stringBuilder = new StringBuilder();
        if (indices[0] < 0) {
            ParameterFormatter.formatMessage(stringBuilder, format, arguments, usedCount);
        } else {
            ParameterFormatter.formatMessage2(stringBuilder, format, arguments, usedCount, indices);
        }
        return stringBuilder.toString();
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }

}