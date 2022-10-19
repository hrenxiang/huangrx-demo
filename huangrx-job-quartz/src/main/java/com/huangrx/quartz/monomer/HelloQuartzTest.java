package com.huangrx.quartz.monomer;

import com.huangrx.quartz.job.HelloQuartz;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author hrenxiang
 * @since 2022-10-19 17:22:25
 */
public class HelloQuartzTest {
    public static void main(String[] args) {
            try {
                //定义一个JobDetail
                JobDetail jobDetail = JobBuilder.newJob(HelloQuartz.class)
                        //定义name和group 给触发器一些属性 比如名字，组名。 （可以不写）
                        .withIdentity("job1", "group1")
                        //job需要传递的内容 具体job传递参数。  （可以不写）
                        .usingJobData("name", "huangrx")
                        .build();

                //定义一个Trigger
                Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
                        //加入 scheduler之后立刻执行 立刻启动
                        .startNow()
                        //定时 ，每隔1秒钟执行一次 以某种触发器触发。  （可以不写）
                        .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2)
                        //重复执行
                        .repeatForever())
                        .build();

                //创建scheduler
                Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
                scheduler.scheduleJob(jobDetail, trigger);
                // Scheduler只有在调用start()方法后，才会真正地触发trigger（即执行job）
                scheduler.start(); //运行一段时间后关闭
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Scheduler调用shutdown()方法时结束
                scheduler.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}