package com.huangrx.evenlistener.translation.service;

import com.huangrx.evenlistener.translation.config.Container;
import com.huangrx.evenlistener.translation.config.TestEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * @author hrenxiang
 * @since 2022-07-05 14:01
 */
@Slf4j
@Service
public class ApiService {

    @Resource
    private UserService userService;

    @Resource
    ApiService2 apiService2;

    @Qualifier("userAsyncTaskExecutor")
    @Resource
    Executor executor;

    @Resource
    ApplicationContext applicationContext;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void update(String name) {
        userService.updateEmpNameById(7369, name);
        //apiService2.sendMessage(7369);
        applicationContext.publishEvent(new TestEvent(7369));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Container.threadLocal().set(name);
        System.out.println("ApiService");
    }

    @Async
    public void sendMessage(Integer id) {
        System.out.println("=====" + userService.selectEmpNameById(id));
    }

    public void testAsync() {
        Container.threadLocal().set("黄某人，你真帅");
        executor.execute(() -> {
            log.info("你好不要脸啊！！！");
        });
    }

    //@Async("userAsyncTaskExecutor")
    //@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    //public void sendMessage2(Integer id) {
    //    String name = userService.selectEmpNameById(id);
    //    log.info("=====" + name);
    //    Container.threadLocal().set(name);
    //}

}
