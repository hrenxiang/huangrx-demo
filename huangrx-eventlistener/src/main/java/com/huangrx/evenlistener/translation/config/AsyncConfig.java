package com.huangrx.evenlistener.translation.config;

import com.huangrx.evenlistener.translation.config.Container;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义线程池
 *
 * -- @RefreshScope 配置此注解，实现spring cloud 配置实例热加载 （此模块未引入，暂不配置）
 * -- @EnableAsync  启用spring异步方法执行
 *
 * -- 运行过程
 *     新任务提交时：若当前运行的线程数量小于核心线程数，则新开一个线程;
 *     若已经超过核心线程数，则先放入队列中； 队列满后，检查活动线程是否达到最大线程数，没有则新开一个线程;
 *     当线程总数等于最大线程数时，则执行拒绝策略
 *
 * -- 拒绝策略
 *    AbortPolicy，直接抛出RejectedExecutionException
 *    CallerRunsPolicy，直接在主线程中执行
 *    DiscardOldestPolicy 抛弃队列头的任务，然后重试execute。
 *    DiscardPolicy，直接丢弃
 *
 * @author hrenxiang
 * @since 2022-07-21 15:52
 */
@Slf4j
@EnableAsync
@Configuration
public class AsyncConfig {

    /**
     * 核心线程数
     */
    @Value("${async.task-executor.core-pool-size:5}")
    private Integer corePoolSize;

    /**
     * 空闲线程存活时间
     */
    @Value("${async.task-executor.keep-alive-seconds:60}")
    private Integer keepAliveSeconds;

    /**
     * 最大线程数
     */
    @Value("${async.task-executor.max-pool-size:50}")
    private Integer maxPoolSize;

    /**
     * 队列大小
     */
    @Value("${async.task-executor.queue-capacity:20}")
    private Integer queueCapacity;

    /**
     * 线程名称前缀
     */
    @Value("${async.task-executor.threadNamePrefix:USER_ASYNC_THREAD_}")
    private String threadNamePrefix;

    @Qualifier
    @Bean(name = "userAsyncTaskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.setThreadNamePrefix(threadNamePrefix);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        taskExecutor.setTaskDecorator(new MyTaskDecorator());
        taskExecutor.initialize();
        return taskExecutor;
    }

    /**
     * 线程装饰器
     */
    static class MyTaskDecorator implements TaskDecorator {

        @Override
        public @NonNull Runnable decorate(@NonNull Runnable runnable) {

            String res = Container.threadLocal().get();
            log.info("【MyTaskDecorator】- 装饰前：{}", res);
            return () -> {
                try {
                    // 将变量重新放入run线程中
                    Container.threadLocal().set(res);
                    log.info("【MyTaskDecorator】- 线程池线程开始运行，container：{}",
                            Container.threadLocal().get());
                    runnable.run();
                } finally {
                    Container.threadLocal().remove();
                    log.info("【MyTaskDecorator】- 线程池线程结束运行，container：{}",
                            Container.threadLocal().get() == null ? '空':Container.threadLocal().get());
                }
            };

        }

        //@Override
        //public Runnable decorate(Runnable runnable) {
        //    try {
        //        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        //        SecurityContext securityContext = SecurityContextHolder.getContext();
        //        return () -> {
        //            try {
        //                RequestContextHolder.setRequestAttributes(attributes);
        //                SecurityContextHolder.setContext(securityContext);
        //                runnable.run();
        //            } finally {
        //                RequestContextHolder.resetRequestAttributes();
        //            }
        //        };
        //    } catch (IllegalStateException e) {
        //        return runnable;
        //    }
        //}
    }
}
