package com.huangrx.rabbitmq.mq;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * RabbitMqUtils
 *
 * @author    hrenxiang
 * @since    2022/4/28 11:10 AM
 */
@Slf4j
@Component
public class RabbitMqUtils implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback{

    private final RabbitTemplate rabbitTemplate;

    /**
     * 构造方法注入，初始化rabbitTemplate
     * 如果rabbitTemplate设置为单例bean，
     * 则所有的rabbitTemplate实际的ConfirmCallback为最后一次申明的ConfirmCallback。
     *
     * @param rabbitTemplate rabbitTemplate 提供了丰富的有关消息的方法
     */
    @Autowired
    public RabbitMqUtils(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        //消息发送成功或失败回调 此方法中的 confirm
        rabbitTemplate.setConfirmCallback(this);
        //如果设置备份队列则不起作用
        rabbitTemplate.setMandatory(true);
        //回退模式： 当消息发送给Exchange后,Exchange路由到Queue失败时 才会执行 ReturnCallBack 此方法中的 returnedMessage
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     * 回调确认
     * @param correlationData 数据
     * @param ack 确认
     * @param cause 原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if(ack){
            log.info("消息发送成功：correlationData({}),ack({}),cause({})",correlationData, Boolean.TRUE, cause);
        }else{
            log.info("消息发送失败：correlationData({}),ack({}),cause({})",correlationData, Boolean.FALSE, cause);
        }
    }

    /**
     * 消息发送到转换器的时候没有对列,配置了备份对列该回调则不生效
     * @param message 消息
     * @param replyCode replyCode
     * @param replyText replyText
     * @param exchange 交换机
     * @param routingKey 路由
     */
    @Override
    public void returnedMessage(@NonNull Message message, int replyCode, @NonNull String replyText, @NonNull String exchange, @NonNull String routingKey) {
        log.info("消息丢失：exchange({}),route({}),replyCode({}),replyText({}),message:{}",exchange,routingKey,replyCode,replyText,message);
    }

    /**
     * 发送到指定Queue
     * @param queueName 队列名称
     * @param obj 消息内容
     */
    public void send(String queueName, Object obj){
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        log.info("callbackSender UUID: " + correlationData.getId());
        this.rabbitTemplate.convertAndSend(queueName, obj, correlationData);
    }

    /**
     * 根据交换机和routingKey 发送消息
     * @param exChange 交换机名称
     * @param routingKey routingKey
     * @param obj 消息内容
     */
    public void sendByRoutingKey(String exChange, String routingKey, Object obj){
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        log.info("callbackSender UUID: " + correlationData.getId());
        this.rabbitTemplate.convertAndSend(exChange, routingKey, obj, correlationData);
    }
}
