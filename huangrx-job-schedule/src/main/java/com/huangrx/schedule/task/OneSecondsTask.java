package com.huangrx.schedule.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 一秒执行一次
 *
 * @author hrenxiang
 * @since 2022-04-26 8:31 PM
 */
@Slf4j
@Component
public class OneSecondsTask {

    @Scheduled(fixedDelay = 1000)
    public void runScheduleFixedRate() {
        log.info("runScheduleFixedRate: current DateTime, {}", LocalDateTime.now());
    }
}
