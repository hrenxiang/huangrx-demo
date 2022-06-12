package com.huangrx.provider.exception;

import com.huangrx.provider.domain.response.IErrorCode;
import org.springframework.util.StringUtils;

/**
 * 断言处理类，用于抛出各种API异常
 *
 * @author hrenxiang
 * @since 2022-04-24 9:48 PM
 */
public class Asserts {

    /**
     * 失败，抛出消息
     * @param message 消息
     */
    public static void fail(String message) {
        throw new ApiException(message);
    }

    /**
     * 失败，抛出错误码
     * @param errorCode 错误码
     */
    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }

    /**
     * 如果不是true，则抛异常
     */
    public static void isTrue(boolean expression, String msg) {
        if (!expression) {
            throw new ApiException(msg);
        }
    }
    /**
     * 如果是true，则抛异常
     */
    public static void isFalse(boolean expression, String msg) {
        if (expression) {
            throw new ApiException(msg);
        }
    }

    /**
     * 如果空或者空字符串，则抛异常
     */
    public static void isNotEmpty(String str, String msg) {
        if (StringUtils.isEmpty(str)) {
            throw new ApiException(msg);
        }
    }
    /**
     * 如果数组为空或者长度小于1，则抛异常
     */
    public static void isNotEmpty(Object[] strArr, String msg) {
        if (strArr == null || strArr.length < 1 ) {
            throw new ApiException(msg);
        }
    }
    
}
