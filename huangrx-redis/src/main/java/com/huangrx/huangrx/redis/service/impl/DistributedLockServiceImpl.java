package com.huangrx.huangrx.redis.service.impl;

import com.google.common.collect.Lists;
import com.huangrx.huangrx.redis.service.CacheConstants;
import com.huangrx.huangrx.redis.service.DistributedLockService;
import com.huangrx.huangrx.redis.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author hrenxiang
 * @since 2022-09-08 10:26
 */
@Service
public class DistributedLockServiceImpl implements DistributedLockService {

    @Resource
    private RedisService redisService;

    @Override
    public void testLock() {
        // 查询redis中的num值 测试前记得先添加这个进入缓存，不然 一直是空，一直return
        Object value = this.redisService.get(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "testLock", "1"));
        // 没有该值return
        if (Objects.isNull(value)) {
            return;
        }
        // 有值就转成成int
        Integer num = (Integer) value;
        // 把redis中的num值+1
        this.redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "testLock", "1"), ++num);
    }

    @Override
    public synchronized void testSynchronizedLock() {
        // 查询redis中的num值 测试前记得先添加这个进入缓存，不然 一直是空，一直return
        Object value = this.redisService.get(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "testSynchronizedLock", "1"));
        // 没有该值return
        if (Objects.isNull(value)) {
            return;
        }
        // 有值就转成成int
        Integer num = (Integer) value;
        // 把redis中的num值+1
        this.redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "testSynchronizedLock", "1"), ++num);
    }

    @Override
    public void testSetNxLock() {
        // 1. 从redis中获取锁,setnx
        //Boolean lock = redisService.setNx(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "lock"), "locked");

        // 修改，设置过期时间，防止业务逻辑出现异常，导致锁无法释放
        Boolean lock = redisService.setNx(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "lock"), "locked", 3, TimeUnit.SECONDS);
        if (lock) {
            // 查询redis中的num值
            Integer value = (Integer) redisService.get(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "num"));
            // 没有该值return
            if (Objects.isNull(value)) {
                return;
            }
            // 把redis中的num值+1
            redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "num"), ++value);

            // 2. 释放锁 del
            redisService.del(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "lock"));
        } else {
            // 3. 每隔1秒钟回调一次，再次尝试获取锁
            try {
                Thread.sleep(1000);
                testSetNxLock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void testSetNxUuidLock() {
        // 1. 从redis中获取锁,setnx， 使用uuid 防止误删除
        String uuid = UUID.randomUUID().toString();
        // 2. 设置过期时间，防止业务异常无法释放锁
        Boolean lock = redisService.setNx(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "lock"), uuid, 3, TimeUnit.SECONDS);
        if (lock) {
            // 查询redis中的num值
            Integer value = (Integer) redisService.get(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "num"));
            // 没有该值return
            if (Objects.isNull(value)) {
                return;
            }
            // 把redis中的num值+1
            redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "num"), ++value);

            // 3. 释放锁 del, 判断UUID是否是加锁时的UUID，如果是再进行删除，防止误删
            if (StringUtils.equals((String) redisService.get(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "lock")), uuid)) {
                redisService.del(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "lock"));
            }

        } else {
            // 4. 每隔1秒钟回调一次，再次尝试获取锁
            try {
                Thread.sleep(1000);
                testSetNxLock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void testSetNxLuaLock() {
        // 1. 从redis中获取锁,setnx， 使用uuid 防止误删除
        String uuid = UUID.randomUUID().toString();
        // 2. 给锁设置过期时间，防止业务异常无法释放锁
        Boolean lock = redisService.setNx(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "lock"), uuid, 3, TimeUnit.SECONDS);
        if (lock) {
            // 查询redis中的num值
            Integer value = (Integer) redisService.get(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "num"));
            // 没有该值return
            if (Objects.isNull(value)) {
                return;
            }
            // 把redis中的num值+1
            redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "num"), ++value);

            // 3. 释放锁 del, 使用lua脚本，提供原子性（a执行删除，查询uuid相等；a setNx 获得的锁刚好到期，b进来获取到锁，导致a删除的时候，删除的是b的锁）
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            redisService.execute(script, Boolean.class, Collections.singletonList(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "lock")), uuid);

        } else {
            // 4. 每隔1秒钟回调一次，再次尝试获取锁
            try {
                Thread.sleep(1000);
                testSetNxLock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 可重入锁 --- 加锁
     *
     * @param lockName 锁名
     * @param uuid     标识防误删
     * @param expire   锁过期时间 单位：秒
     * @return 是否获得锁
     */
    @Override
    public Boolean tryReentrantLock(String lockName, String uuid, Long expire) {
        String script = "if (redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) == 1) " +
                "then" +
                "    redis.call('hincrby', KEYS[1], ARGV[1], 1);" +
                "    redis.call('expire', KEYS[1], ARGV[2]);" +
                "    return 1;" +
                "else" +
                "   return 0;" +
                "end";
        if (!redisService.execute(script, Boolean.class, Collections.singletonList(lockName), uuid, expire)) {
            try {
                // 没有获取到锁，重试
                Thread.sleep(200);
                tryReentrantLock(lockName, uuid, expire);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 锁续期, 测试自动续期时加上这个
        // this.renewTime(lockName, uuid, expire * 1000);

        // 获取到锁，返回true
        return true;
    }

    /**
     * 可重入锁 --- 解锁
     *
     * @param lockName 锁名称
     * @param uuid     标识防止误删
     */
    @Override
    public void unReentrantLock(String lockName, String uuid) {
        String script = "if (redis.call('hexists', KEYS[1], ARGV[1]) == 0) then" +
                "    return nil;" +
                "end;" +
                "if (redis.call('hincrby', KEYS[1], ARGV[1], -1) > 0) then" +
                "    return 0;" +
                "else" +
                "    redis.call('del', KEYS[1]);" +
                "    return 1;" +
                "end;";
        // 这里之所以没有跟加锁一样使用 Boolean ,这是因为解锁 lua 脚本中，三个返回值含义如下：
        // 1 代表解锁成功，锁被释放
        // 0 代表可重入次数被减 1
        // null 代表其他线程尝试解锁，解锁失败
        Long result = redisService.execute(script, Long.class, Lists.newArrayList(lockName), uuid);
        // 如果未返回值，代表尝试解其他线程的锁
        if (result == null) {
            throw new IllegalMonitorStateException("attempt to unlock lock, not locked by lockName: "
                    + lockName + " with request: " + uuid);
        }
    }

    /**
     * 锁延期
     * 线程等待超时时间的1/3时间后,执行锁延时代码,直到业务逻辑执行完毕,因此在此过程中,其他线程无法获取到锁,保证了线程安全性
     * @param lockName 锁名称
     * @param expire 单位：毫秒
     */
    @Override
    public void renewTime(String lockName, String uuid, Long expire){
        String script = "if(redis.call('hexists', KEYS[1], ARGV[1]) == 1) then redis.call('expire', KEYS[1], ARGV[2]); return 1; else return 0; end";
        new Thread(() -> {
            while (redisService.execute(script, Boolean.class, Lists.newArrayList(lockName), uuid, expire.toString())){
                try {
                    // 每当过期时间还有2/3时间，自动续期
                    Thread.sleep(expire / 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
