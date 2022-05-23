package com.huangrx.huangrx.redis.util;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.Objects;

/**
 * 工具类
 *
 * @author hrenxiang
 * @since 2022-04-24 10:16 PM
 */
public final class ObjectValCheck {

    public ObjectValCheck() {
    }

    public static boolean isTrue(Boolean obj) {
        return obj != null ? obj : false;
    }

    public static boolean isFalse(Boolean obj) {
        return obj == null || !obj;
    }

    public static boolean isZero(Integer obj) {
        return obj != null && Objects.equals(NumberUtils.INTEGER_ZERO, obj);
    }

    public static boolean isOne(Integer obj) {
        return obj != null && Objects.equals(NumberUtils.INTEGER_ONE, obj);
    }
}