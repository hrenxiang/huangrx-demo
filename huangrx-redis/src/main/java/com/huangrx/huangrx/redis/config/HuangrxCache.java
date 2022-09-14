package com.huangrx.huangrx.redis.config;

import java.lang.annotation.*;

/**
 * 自定义缓存注解
 * @author        hrenxiang
 * @since         2022-09-14 10:23:14
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HuangrxCache {

    /**
     * 缓存的前缀
     * @return
     */
    String prefix() default "";

    /**
     * 设置缓存的有效时间
     * 单位：分钟
     * @return
     */
    int timeout() default 5;

    /**
     * 防止雪崩设置的随机值范围
     * @return
     */
    int random() default 5;

    /**
     * 防止击穿，分布式锁的key
     * @return
     */
    String lock() default "lock";
}