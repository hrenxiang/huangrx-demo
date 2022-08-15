package com.huangrx.unifiedlog.mdc;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * MDC demo
 *
 * 一定要新增 logback.xml 文件，不然 mian函数中的日志输出不出来效果
 *
 * @author hrenxiang
 * @since 2022-08-11 17:47
 */
@Slf4j
public class MdcController {

    private static final Logger logger = LoggerFactory.getLogger(MdcController.class);

    public static void main(String[] args) {
        //MDC.put("trace_id", UUID.randomUUID().toString());
        //logger.info("============= 1");
        //MDC.remove("trace_id");
        //logger.info("============= 2");


        MultiThreadHandler multiThreadHandler1 = new MultiThreadHandler();
        MultiThreadHandler multiThreadHandler2 = new MultiThreadHandler();

        new Thread(multiThreadHandler1).start();
        new Thread(multiThreadHandler2).start();

    }

    @Slf4j
    static class MultiThreadHandler implements Runnable {

        @Override
        public void run() {
            MDC.put("trace_id", UUID.randomUUID().toString());
            log.info("多线程：{} ============= 处理服务 开始", Thread.currentThread().getName());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("多线程：{} ============= 处理服务 结束", Thread.currentThread().getName());
            MDC.remove("trace_id");
            log.info("多线程: {} ============= 避免内存泄露 释放", Thread.currentThread().getName());
        }
    }
}
