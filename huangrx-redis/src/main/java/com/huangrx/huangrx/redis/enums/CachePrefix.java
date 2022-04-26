package com.huangrx.huangrx.redis.enums;

/**
 * 缓存的前缀
 *
 * @author hrenxiang
 * @since 2022-04-24 9:48 PM
 */
public enum CachePrefix {

    /**
     * 我的信息
     */
    MY_MESSAGE;


    public String getPrefix() {
        return this.name() + "_";
    }
}