package com.huangrx.threadlocal.config;

/**
 * threadLocal 容器
 *
 * @author hrenxiang
 * @since 2022-05-19 3:23 PM
 */
public class Container {

    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    public static ThreadLocal<String> threadLocal() {
        return THREAD_LOCAL;
    }
}
