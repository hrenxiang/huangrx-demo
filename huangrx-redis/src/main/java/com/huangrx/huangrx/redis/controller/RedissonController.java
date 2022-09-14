package com.huangrx.huangrx.redis.controller;

import com.huangrx.huangrx.redis.domain.BaseResponse;
import com.huangrx.huangrx.redis.service.CacheConstants;
import com.huangrx.huangrx.redis.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Redisson
 *
 * @author hrenxiang
 * @since 2022-09-13 18:29
 */
@RestController
public class RedissonController {
    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedisService redisService;

    @Resource
    private ApplicationContext applicationContext;

    @RequestMapping(value = "/redisson/testLock", method = RequestMethod.GET)
    public void testLock() {

        System.out.println("redisson test lock: " + applicationContext.getEnvironment().getProperty("server.port"));

        // 只要锁的名称相同就是同一把锁
        RLock lock = this.redissonClient.getLock(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_REDISSON_KEY, "REDISSON", "LOCK"));
        // 加锁
        lock.lock();

        // 查询redis中的num值
        Object value = redisService.get(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_REDISSON_KEY, "REDISSON", "NUM"));
        // 没有该值return
        if (Objects.isNull(value)) {
            redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_REDISSON_KEY, "REDISSON", "NUM"), 1);
            lock.unlock();
            return;
        }
        // 有值就转成成int
        int num =Integer.parseInt(value.toString());
        // 把redis中的num值+1
        redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_REDISSON_KEY, "REDISSON", "NUM"), String.valueOf(++num));
        // 解锁
        lock.unlock();
    }

    @RequestMapping(value = "/redisson/testAutomaticReleaseLock", method = RequestMethod.GET)
    public BaseResponse<String> testAutomaticReleaseLock() {

        System.out.println("redisson test lock: " + applicationContext.getEnvironment().getProperty("server.port"));

        // 只要锁的名称相同就是同一把锁
        RLock lock = this.redissonClient.getLock(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_REDISSON_KEY, "REDISSON", "LOCK"));
        // 加锁
        lock.lock(10, TimeUnit.SECONDS);

        // 查询redis中的num值
        Object value = redisService.get(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_REDISSON_KEY, "REDISSON", "NUM"));
        // 没有该值return
        if (Objects.isNull(value)) {
            redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_REDISSON_KEY, "REDISSON", "NUM"), 1);
            return BaseResponse.success("结束 ～～～！");
        }
        // 有值就转成成int
        int num =Integer.parseInt(value.toString());
        // 把redis中的num值+1
        redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_REDISSON_KEY, "REDISSON", "NUM"), String.valueOf(++num));
        return BaseResponse.success("结束 ～～～！");
    }
}
