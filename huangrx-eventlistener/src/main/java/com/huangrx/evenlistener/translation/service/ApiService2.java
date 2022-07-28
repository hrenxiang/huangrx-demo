package com.huangrx.evenlistener.translation.service;

import com.huangrx.evenlistener.translation.config.Container;
import com.huangrx.evenlistener.translation.config.TestEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;

/**
 * @author hrenxiang
 * @since 2022-07-21 11:25
 */
@Slf4j
@Service
public class ApiService2 {
    @Resource
    UserService userService;
    @Async
    public void sendMessage(Integer id) {
        System.out.println("=====" + userService.selectEmpNameById(id));
    }

    @Async("userAsyncTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendMessage2(TestEvent event) {
        log.info("开始开始开始: {}", event.getId());
        String name = userService.selectEmpNameById(event.getId());
        log.info("=====" + name);
        log.info("结束结束");
    }
}
