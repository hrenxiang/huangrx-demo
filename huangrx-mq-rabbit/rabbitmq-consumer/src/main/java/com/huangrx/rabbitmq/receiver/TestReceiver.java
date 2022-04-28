package com.huangrx.rabbitmq.receiver;

import com.huangrx.rabbitmq.constants.RabbitMqConstants;
import com.huangrx.rabbitmq.mq.MessageBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 接受消息类
 *
 * @author hrenxiang
 * @since 2022-04-28 11:39 AM
 */
@Slf4j
@Component
public class TestReceiver {

    /**
     * 接受 队列 TEST1_QUEUE 中的消息
     * @param msg 封装的消息内容
     */
    @RabbitListener(queues = {RabbitMqConstants.TEST1_QUEUE})
    public void process(MessageBase<String> msg) {
        log.info("Receiver --- {}", msg.getMessage());
    }
}
