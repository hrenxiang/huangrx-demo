package com.huangrx.aop;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;

/**
 * @author        hrenxiang
 * @since         2022-09-22 09:26:48
 */
@Component
@Aspect
@Slf4j
public class TimingUtil {

    @Pointcut("execution(* com.huangrx.aop..*.*(..))")
    private void pointCut(){}

    @Around("pointCut()")
    public Object timing(ProceedingJoinPoint joinPoint){
        Object object;
        StopWatch stopWatch = new StopWatch("任务耗时");
        try{
            Signature signature = joinPoint.getSignature();
            String methodName = signature.getName();
            stopWatch.start(methodName);
            object = joinPoint.proceed();
            stopWatch.stop();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
        log.info("API request completed, The time spent is as follows.");
        log.info(stopWatch.prettyPrint());
        log.info(String.valueOf(stopWatch.getTotalTimeNanos()));
        log.info(String.valueOf(stopWatch.getTotalTimeMillis()));
        log.info(String.valueOf(stopWatch.getTotalTimeSeconds()));
        return object;
    }
}