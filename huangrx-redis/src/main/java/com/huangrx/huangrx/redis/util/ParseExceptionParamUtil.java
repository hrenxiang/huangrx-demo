package com.huangrx.huangrx.redis.util;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * @author hrenxiang
 * @since 2022-09-07 16:04
 */
public class ParseExceptionParamUtil {
    private static int[] indices;
    private static int usedCount;
    private static Throwable throwable;

    public static String parseParameters(String format, Object... arguments) {
        init(format, arguments);
        String message = formatMessage(format, arguments);
        if (throwable != null) {
            message += System.lineSeparator() + ExceptionUtils.getStackTrace(throwable);
        }

        return message;
    }

    private static void init(String format, Object... arguments) {
        // divide by 2
        final int len = Math.max(1, format == null ? 0 : format.length() >> 1);
        // LOG4J2-1542 ensure non-zero array length
        indices = new int[len];
        final int placeholders = ParameterFormatter.countArgumentPlaceholders2(format, indices);
        initThrowable(arguments, placeholders);
        usedCount = Math.min(placeholders, arguments == null ? 0 : arguments.length);
    }

    private static void initThrowable(final Object[] params, final int usedParams) {
        if (params != null) {
            final int argCount = params.length;
            if (usedParams < argCount && throwable == null && params[argCount - 1] instanceof Throwable) {
                throwable = (Throwable) params[argCount - 1];
            }
        }
    }

    private static String formatMessage(String format, Object... arguments) {
        StringBuilder stringBuilder = new StringBuilder();
        if (indices[0] < 0) {
            ParameterFormatter.formatMessage(stringBuilder, format, arguments, usedCount);
        } else {
            ParameterFormatter.formatMessage2(stringBuilder, format, arguments, usedCount, indices);
        }
        return stringBuilder.toString();
    }
}
