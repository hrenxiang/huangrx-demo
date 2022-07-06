package com.huangrx.distributedid;

import com.huangrx.distributedid.service.CacheConstants;
import com.huangrx.distributedid.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * redis单体应用 使用 incr 原子操作是使id自增
 *
 * @author hrenxiang
 * @since 2022-07-06 16:38
 */
@SpringBootTest
public class RedisSingleDemoTest {

    @Autowired
    RedisService redisService;

    @Test
    void test() {
        String aOrderKey = CacheConstants.generateKey(CacheConstants.TEST_KEY, "a_order");
        redisService.incr(aOrderKey, 1);

        System.out.println(redisService.get(aOrderKey));
    }
}
