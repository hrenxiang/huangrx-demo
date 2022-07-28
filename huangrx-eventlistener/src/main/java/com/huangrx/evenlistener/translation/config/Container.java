package com.huangrx.evenlistener.translation.config;

/**
 * @author hrenxiang
 * @since 2022-07-21 16:32
 */
public class Container {
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    public static ThreadLocal<String> threadLocal() {
        return THREAD_LOCAL;
    }
}
