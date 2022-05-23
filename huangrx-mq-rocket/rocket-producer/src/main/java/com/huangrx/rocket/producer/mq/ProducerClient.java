package com.huangrx.rocket.producer.mq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息生产者客户端
 *
 * @author hrenxiang
 * @since 2022-05-23 4:54 PM
 */
@Configuration
public class ProducerClient {

    private final RocketMqConfig mqConfig;

    public ProducerClient(RocketMqConfig mqConfig) {
        this.mqConfig = mqConfig;
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public DefaultMQProducer buildProducerBean() {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer(mqConfig.getGroup());
        // 设置NameServer的地址
        producer.setNamesrvAddr(mqConfig.getNameServerAddr());
        // 设置发送消息超时时间
        producer.setSendMsgTimeout(mqConfig.getSendMsgTimeout());
        // 设置重试次数
        producer.setRetryTimesWhenSendFailed(mqConfig.getFailureRetryNumber());
        return producer;
    }
}
