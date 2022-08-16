package com.huangrx.rabbitmq.receiver;

import com.huangrx.rabbitmq.constants.RabbitMqConstants;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 接受消息类 手动接收确认消息
 *
 * @author hrenxiang
 * @since 2022-04-28 11:39 AM
 */
@Slf4j
@Component
public class TestReceiver {

    private static Integer COUNT = 1;

    /**
     * 接受 队列 TEST1_QUEUE 中的消息
     * @param message 封装的消息内容
     */
    @RabbitListener(queues = {RabbitMqConstants.TEST1_QUEUE})
    public void process(Message message, Channel channel) throws IOException {
//        try {
//            log.info("received --- {}", message);
//            log.info("received --- {}", new String(message.getBody(), StandardCharsets.UTF_8));

            log.info("重试次数 --- {}", COUNT++);

            //TODO 具体业务
            int i = 1/0;

//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

//        }  catch (Exception e) {
//
//            if (message.getMessageProperties().getRedelivered()) {
//
//                log.error("The message has been processed repeatedly and failed to be received again.");
//
//                // 拒绝消息
//                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
//            } else {
//
//                log.error("The message is about to return to the queue again for processing.");
//
//                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
//            }
//        }

    }
}
