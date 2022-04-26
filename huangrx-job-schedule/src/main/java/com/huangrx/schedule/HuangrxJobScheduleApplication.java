package com.huangrx.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author    hrenxiang
 * @since    2022/4/26 8:24 PM
 */
@EnableScheduling
@SpringBootApplication
public class HuangrxJobScheduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuangrxJobScheduleApplication.class, args);
    }

}
