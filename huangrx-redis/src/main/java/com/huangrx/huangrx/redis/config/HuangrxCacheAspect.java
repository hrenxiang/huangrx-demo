package com.huangrx.huangrx.redis.config;

import com.alibaba.fastjson.JSON;
import com.huangrx.huangrx.redis.service.RedisService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 缓存切面
 *
 * @author        hrenxiang
 * @since         2022-09-14 10:23:45
 */
@Aspect
@Component
public class HuangrxCacheAspect {

    @Resource
    private RedisService redisService;

    @Resource
    private RedissonClient redissonClient;

    /**
     * joinPoint.getArgs(); 获取方法参数
     * joinPoint.getTarget().getClass(); 获取目标类
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.huangrx.huangrx.redis.config.HuangrxCache)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        // 获取切点方法的签名
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        // 获取方法对象
        Method method = signature.getMethod();
        // 获取方法上指定注解的对象
        HuangrxCache annotation = method.getAnnotation(HuangrxCache.class);
        // 获取注解中的前缀
        String prefix = annotation.prefix();
        // 获取方法的参数
        Object[] args = joinPoint.getArgs();
        String param = Arrays.asList(args).toString();
        // 获取方法的返回值类型
        Class<?> returnType = method.getReturnType();

        // 拦截前代码块：判断缓存中有没有
        Object json = redisService.get(prefix + param);
        // 判断缓存中的数据是否为空
        if (Objects.nonNull(json)){
            return JSON.parseObject(json.toString(), returnType);
        }

        // 没有，加分布式锁
        String lock = annotation.lock();
        RLock rLock = this.redissonClient.getLock(lock + param);
        rLock.lock();

        // 判断缓存中有没有，有直接返回(加锁的过程中，别的请求可能已经把数据放入缓存)
        Object json2 = redisService.get(prefix + param);
        // 判断缓存中的数据是否为空
        if (Objects.nonNull(json2)){
            rLock.unlock();
            return JSON.parseObject(json2.toString(), returnType);
        }

        // 执行目标方法
        Object result = joinPoint.proceed(joinPoint.getArgs());

        // 拦截后代码块：放入缓存 释放分布锁
        int timeout = annotation.timeout();
        int random = annotation.random();
        redisService.set(prefix + param, JSON.toJSONString(result), timeout + new Random().nextInt(random), TimeUnit.MINUTES);
        rLock.unlock();

        return result;
    }
}