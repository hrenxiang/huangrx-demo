package com.huangrx.huangrx.redis.service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 缓存的常量
 *
 * @author hrenxiang
 * @since 2022-04-24 9:48 PM
 */
public interface CacheConstants {

    String TEST_KEY = "TEST";
    String DISTRIBUTED_LOCK_KEY = "DISTRIBUTED_LOCK";
    String DISTRIBUTED_REDISSON_KEY = "DISTRIBUTED_REDISSON";

    /**
     * 生成 redis key
     *
     * @param namespace 空间
     * @param params 参数
     * @return redis key
     */
    static String generateKey(String namespace, Object... params) {
        return namespace +
                ":" +
                Stream.of(params).map(Object::toString).collect(Collectors.joining("_"));
    }

}
