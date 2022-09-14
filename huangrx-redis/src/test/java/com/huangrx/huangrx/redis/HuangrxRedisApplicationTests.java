package com.huangrx.huangrx.redis;

import com.huangrx.huangrx.redis.service.CacheConstants;
import com.huangrx.huangrx.redis.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.*;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

@SpringBootTest
class HuangrxRedisApplicationTests {

    @Resource
    private RedisService redisService;

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        String uuid = UUID.randomUUID().toString();
        String script = "redis.call('set', KEYS[1], ARGV[1]);";
        redisService.execute(script, Boolean.class, Collections.singletonList(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "TEST_LUA", "LUA")), uuid);
    }

}
