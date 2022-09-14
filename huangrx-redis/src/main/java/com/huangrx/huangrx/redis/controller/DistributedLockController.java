package com.huangrx.huangrx.redis.controller;

import com.huangrx.huangrx.redis.domain.BaseResponse;
import com.huangrx.huangrx.redis.service.CacheConstants;
import com.huangrx.huangrx.redis.service.DistributedLockService;
import com.huangrx.huangrx.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 *
 * @author hrenxiang
 * @since 2022-09-07 18:14
 */
@Slf4j
@RestController
public class DistributedLockController {

    @Resource
    private DistributedLockService distributedLockService;

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private RedisService redisService;

    /**
     * ab -n 500 -c 10 http://localhost:8081/index/testLock  95
     * ab -n 500 -c 1 http://localhost:8081/index/testLock   500
     * @return
     */
    @RequestMapping(value = "index/testLock", method = RequestMethod.GET)
    public BaseResponse<Object> testLock(){
        log.info("开始！");
        distributedLockService.testLock();


        return BaseResponse.success(applicationContext.getEnvironment().getProperty("server.port") + "你好啊，这可不是分布式锁！");
    }

    @RequestMapping(value = "index/testSynchronizedLock", method = RequestMethod.GET)
    public BaseResponse<Object> testSynchronizedLock(){
        log.info("开始！");
        distributedLockService.testSynchronizedLock();

        return BaseResponse.success("你好啊，这可不是分布式锁！");
    }

    @RequestMapping(value = "index/testSetNxLock", method = RequestMethod.GET)
    public BaseResponse<Object> testSetNxLock(){
        log.info("开始！");
        distributedLockService.testSetNxLock();

        return BaseResponse.success("你好啊，这离分布式锁近了！");
    }

    @RequestMapping(value = "index/testSetNxUuidLock", method = RequestMethod.GET)
    public BaseResponse<Object> testSetNxUuidLock(){
        log.info("开始！");
        distributedLockService.testSetNxUuidLock();

        return BaseResponse.success("你好啊，这离分布式锁近了！");
    }

    @RequestMapping(value = "index/testSetNxLuaLock", method = RequestMethod.GET)
    public BaseResponse<Object> testSetNxLuaLock(){
        log.info("开始！");
        distributedLockService.testSetNxLuaLock();

        return BaseResponse.success("你好啊，这离分布式锁近了！");
    }

    @RequestMapping(value = "index/testReentrantLock", method = RequestMethod.GET)
    public BaseResponse<Object> testReentrantLock() {
        // 加锁
        String uuid = UUID.randomUUID().toString();
        Boolean lock = distributedLockService.tryReentrantLock(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "REENTRANT", "LOCK"), uuid, 300L);

        if (lock) {
            // 读取redis中的num值
            Object numString = (Object) redisService.get(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "REENTRANT", "NUM"));
            if (Objects.isNull(numString)) {
                redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "REENTRANT", "NUM"), 1);
                distributedLockService.unReentrantLock(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "REENTRANT", "LOCK"), uuid);
                return BaseResponse.success(applicationContext.getEnvironment().getProperty("server.port") + "添加可重入！");
            }

            // ++操作
            Integer num = (Integer) numString;
            num++;

            // 放入redis
            redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "REENTRANT", "NUM"), String.valueOf(num));

            // 测试可重入性
            this.testSubLock(uuid);

            // 释放锁
            distributedLockService.unReentrantLock(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "REENTRANT", "LOCK"), uuid);
        }

        return BaseResponse.success(applicationContext.getEnvironment().getProperty("server.port") + "你好啊，可重入！");
    }

    /**
     * 测试可重入性<br/>
     * <br/>
     * @param uuid 重入前的锁标识，重入 入的还是这个锁
     */
    private void testSubLock(String uuid){
        // 加锁
        Boolean lock = distributedLockService.tryReentrantLock(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "REENTRANT", "LOCK"), uuid, 300L);

        if (lock) {
            System.out.println("分布式可重入锁。。。");

            distributedLockService.unReentrantLock(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "REENTRANT", "LOCK"), uuid);
        }
    }

    /**
     * 测试锁自动续期<br/>
     */
    @RequestMapping(value = "index/testLockRenewTime", method = RequestMethod.GET)
    public void testLockRenewTime() {
        // 加锁
        String uuid = UUID.randomUUID().toString();
        Boolean lock = distributedLockService.tryReentrantLock("lock", uuid, 30L);

        if (lock) {
            // 读取redis中的num值
            String numString = (String) redisService.get("num");
            if (StringUtils.isBlank(numString)) {
                return;
            }

            // ++操作
            Integer num = Integer.parseInt(numString);
            num++;

            // 放入redis
            redisService.set("num", String.valueOf(num));

            // 睡眠60s，锁过期时间30s。每隔20s自动续期
            try {
                TimeUnit.SECONDS.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 测试可重入性
            // this.testSubLock(uuid);

            // 释放锁
            distributedLockService.unReentrantLock("lock", uuid);
        }
    }

}
