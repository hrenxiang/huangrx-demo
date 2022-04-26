package com.huangrx.schedule.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * 一分钟一次的定时任务
 *
 * @author hrenxiang
 * @since 2022-04-26 8:27 PM
 */
@Slf4j
public class OneMinuteTask {

    @Scheduled(fixedDelay = 1000 * 60)
    public void runScheduleFixedRate() {
        log.info("runScheduleFixedRate: current DateTime, {}", LocalDateTime.now());
    }
}
