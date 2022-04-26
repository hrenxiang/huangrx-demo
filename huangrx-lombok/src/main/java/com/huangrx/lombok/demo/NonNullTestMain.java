package com.huangrx.lombok.demo;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

/**
 * ' @NonNull ': 给方法参数增加这个注解会自动在方法内对该参数进行是否为空的校验，如果为空，则抛出`NPE`（NullPointerException）
 *
 * @author hrenxiang
 * @since 2022-04-25 2:12 PM
 */
public class NonNullTestMain {

    public void notNullExample(@NonNull String str) {
        System.out.println(str.length());
    }

    /**
     * 上述 notNullExample 等同于 notNullExample2
     * @param str 字符参数
     */
    public void notNullExample2(String str) {
        if (str != null) {
            System.out.println(str.length());
        } else {
            throw new NullPointerException("null");
        }
    }

    public static void main(String[] args) {
        new NonNullTestMain().notNullExample(StringUtils.EMPTY);
        new NonNullTestMain().notNullExample2(StringUtils.EMPTY);
    }
}
