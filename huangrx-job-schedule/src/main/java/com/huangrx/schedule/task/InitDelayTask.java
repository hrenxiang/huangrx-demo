package com.huangrx.schedule.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 第一次延时启动
 *
 * @author hrenxiang
 * @since 2022-04-26 8:34 PM
 */
@Slf4j
@Component
public class InitDelayTask {

    @Scheduled(initialDelay = 5000, fixedDelay = 1000 * 2)
    public void runScheduleFixedRate() {
        log.info("InitDelayTask runScheduleFixedRate: current DateTime, {}", LocalDateTime.now());
    }
}
