package com.huangrx.rabbitmq.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.nio.charset.StandardCharsets;

/**
 * mq 消息监听器
 *
 * @author    hrenxiang
 * @since    2022/4/28 10:26 AM
 */
@Slf4j
public class MqListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);

            log.info("接收路由名称为：{},路由键为：{},队列名为：{}的消息：{}",
                    message.getMessageProperties().getReceivedExchange(),
                    message.getMessageProperties().getReceivedRoutingKey(),
                    message.getMessageProperties().getConsumerQueue(),
                    msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}