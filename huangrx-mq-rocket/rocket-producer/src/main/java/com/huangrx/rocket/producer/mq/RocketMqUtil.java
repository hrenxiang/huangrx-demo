package com.huangrx.rocket.producer.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author    hrenxiang
 * @since     2022/7/7 09:55
 */
@Slf4j
@Component
public class RocketMqUtil<T> {

    private DefaultMQProducer defaultMQProducer;

    @Autowired
    public RocketMqUtil(DefaultMQProducer defaultMQProducer) {
        this.defaultMQProducer = defaultMQProducer;
    }

    @PostConstruct
    public void init() {
        log.info("【RocketMqUtil】- 助手初始化");
        try {
            defaultMQProducer.start();
        } catch (MQClientException e) {
            log.error("【RocketMqUtil】- 助手初始化失败", e);
        }
    }

    @PreDestroy
    public void destroy() {
        log.info("【RocketMqUtil】- 助手注销");
        defaultMQProducer.shutdown();
    }

    /**
     * 默认CallBack函数
     *
     * @return SendCallback
     */
    private SendCallback getDefaultSendCallBack() {
        return new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("【RocketMqUtil】- 发送MQ成功");
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("【RocketMqUtil】- 发送MQ失败 "+throwable.getMessage(), throwable.getMessage());
            }
        };
    }

    /**
     * 发送单向消息 <br/>
     * 不关心发送结果
     */
    public void sendOneWayMsg(MessageBase<T> message) {
        // 创建消息，并指定Topic，Tag和消息体
        Message msg = new Message(message.getTopic(), message.getTag(), message.getSerializeMessage());
        try {
            // 发送单向消息，没有任何返回结果
            log.info("【发送单向消息】，topic:" + message.getTopic() + ", message:" + message.getMessage());
            defaultMQProducer.sendOneway(msg);
        } catch (Exception e) {
            log.error("【RocketMqUtil】- 【sendOneWayMsg】: 发送 【单向】消息失败！", e);
        }
    }

    /**
     * 发送同步消息 <br/>
     * 通过sendResult返回消息是否成功送达
     *
     * @return SendResult
     */
    public SendResult sendSyncMsg(MessageBase<T> message) {
        SendResult sendResult = new SendResult();
        // 创建消息，并指定Topic，Tag和消息体
        Message msg = new Message(message.getTopic(), message.getTag(), message.getSerializeMessage());
        try {
            log.info("【发送同步消息】，topic:" + message.getTopic() + ", message:" + message.getMessage());
            sendResult = defaultMQProducer.send(msg);
        } catch (Exception e) {
            log.error("【RocketMqUtil】- 【sendSyncMsg】: 发送 【同步】消息失败！", e);
        }
        return sendResult;
    }

    /**
     * 发送异步消息 <br/>
     *
     * @return SendResult
     */
    public void sendAsyncMsg(MessageBase<T> message) {
        // 创建消息，并指定Topic，Tag和消息体
        Message msg = new Message(message.getTopic(), message.getTag(), message.getSerializeMessage());
        try {
            log.info("【发送异步消息】，topic:" + message.getTopic() + ", message:" + message.getMessage());
            defaultMQProducer.send(msg, getDefaultSendCallBack());
        } catch (Exception e) {
            log.error("【RocketMqUtil】- 【sendAsyncMsg】: 发送 【异步】消息失败！", e);
        }
    }

    /**
     * 发送异步消息
     *
     * @param sendCallback 回调函数
     */
    public void sendAsyncMsg(MessageBase<T> message, SendCallback sendCallback) {
        // 创建消息，并指定Topic，Tag和消息体
        Message msg = new Message(message.getTopic(), message.getTag(), message.getSerializeMessage());
        try {
            log.info("【发送异步消息】，topic:" + message.getTopic() + ", message:" + message.getMessage());
            defaultMQProducer.send(msg, sendCallback);
        } catch (Exception e) {
            log.error("【RocketMqUtil】- 【sendAsyncMsg】: 发送 【异步】消息失败！", e);
        }
    }

    /**
     * 发送异步消息
     *
     * @param message      消息实体
     * @param sendCallback 回调函数
     * @param timeout      超时时间
     */
    public void sendAsyncMsg(MessageBase<T> message, SendCallback sendCallback, long timeout) {
        // 创建消息，并指定Topic，Tag和消息体
        Message msg = new Message(message.getTopic(), message.getTag(), message.getSerializeMessage());

        try {
            log.info("【发送异步消息】，topic:" + message.getTopic() + ", message:" + message.getMessage());
            defaultMQProducer.send(msg, sendCallback, timeout);
        } catch (Exception e) {
            log.error("【RocketMqUtil】- 【sendAsyncMsg】: 发送 【异步】消息失败！", e);
        }

    }

    /**
     * 发送异步消息
     *
     * @param message      消息实体
     * @param sendCallback 回调函数
     * @param timeout      超时时间
     * @param delayLevel   延迟消息的级别
     */
    public void sendAsyncMsg(MessageBase<T> message, SendCallback sendCallback, long timeout, int delayLevel) {
        // 创建消息，并指定Topic，Tag和消息体
        Message msg = new Message(message.getTopic(), message.getTag(), message.getSerializeMessage());

        try {
            // 设置延时等级3,这个消息将在10s之后发送(现在只支持固定的几个时间,详看delayTimeLevel)
            msg.setDelayTimeLevel(delayLevel);
            // 发送异步消息，没有任何返回结果
            log.info("【发送异步消息】，topic:" + message.getTopic() + ", timeout:" + timeout + ", delayLevel:" + delayLevel);
            defaultMQProducer.send(msg, sendCallback, timeout);
        } catch (Exception e) {
            log.error("【RocketMqUtil】- 【sendAsyncMsg】: 发送 【异步】消息失败！", e);
        }
    }

    /**
     * 发送顺序消息 <br/>
     *
     * SelectMessageQueueByHash
     *
     * @param message 消息实体
     * @param hashKey 根据哪个字段的hash值进行比较
     */
    public void syncSendOrderly(MessageBase<T> message, String hashKey) {
        // 创建消息，并指定Topic，Tag和消息体
        Message msg = new Message(message.getTopic(), message.getTag(), message.getSerializeMessage());

        try {
            // 发送单向消息，没有任何返回结果
            log.info("【发送顺序消息】，topic:" + message.getTopic() + ", hashKey:" + hashKey);
            defaultMQProducer.send(msg, new SelectMessageQueueByHash(), hashKey);
        } catch (Exception e) {
            log.error("【RocketMqUtil】- 【sendAsyncMsg】: 发送 【异步】消息失败！", e);
        }
    }

    /**
     * 发送顺序消息 <br/>
     *
     * SelectMessageQueueByHash
     *
     * @param message 消息实体
     * @param hashKey 根据哪个字段的hash值进行比较
     * @param timeout 超时时间
     */
    public void syncSendOrderly(MessageBase<T> message, String hashKey, long timeout) {
        // 创建消息，并指定Topic，Tag和消息体
        Message msg = new Message(message.getTopic(), message.getTag(), message.getSerializeMessage());

        try {
            // 发送单向消息，没有任何返回结果
            log.info("【发送顺序消息】，topic:" + message.getTopic() + ", hashKey:" + hashKey);
            defaultMQProducer.send(msg, new SelectMessageQueueByHash(), hashKey, timeout);
        } catch (Exception e) {
            log.error("【RocketMqUtil】- 【sendAsyncMsg】: 发送 【异步】消息失败！", e);
        }
    }

}