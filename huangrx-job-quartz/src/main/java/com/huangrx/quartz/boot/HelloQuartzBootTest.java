package com.huangrx.quartz.boot;

import com.huangrx.quartz.job.HelloQuartz;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hrenxiang
 * @since 2022-10-19 17:47
 */
@Configuration
public class HelloQuartzBootTest {

    @Bean
    public JobDetail getJobDetail() {
        return JobBuilder
                .newJob(HelloQuartz.class)
                .withIdentity("huangrx-boot-jobDetail")
                .usingJobData("name", "huangrx-boot")
                //这个是为了解决：Jobs added with no trigger must be durable.
                .storeDurably(true)
                .build();
    }

    @Bean
    public Trigger getTrigger() {
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2).repeatForever();
        // CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/2 * * * * ?");
        return TriggerBuilder
                .newTrigger()
                .forJob(getJobDetail())
                .withIdentity("huangrx-boot-trigger")
                .startNow()
                .withSchedule(simpleScheduleBuilder)
                .build();
    }

}
