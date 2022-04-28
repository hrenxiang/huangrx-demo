package com.huangrx.rabbitmq.controller;

import com.huangrx.rabbitmq.constants.RabbitMqConstants;
import com.huangrx.rabbitmq.mq.MessageBase;
import com.huangrx.rabbitmq.mq.RabbitMqUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :jhx
 * @date :2020/12/26
 * @desc :
 */
@RestController
@Slf4j
@RequestMapping(value = "/message")
public class TestController {

    private final RabbitMqUtils rabbitMqUtils;

    public TestController(RabbitMqUtils rabbitMqUtils) {
        this.rabbitMqUtils = rabbitMqUtils;
    }

    /**
     * 发送消息test1
     * @param msg 消息内容
     * @return 发送成功
     */
    @PostMapping(value = "/test1")
    public String sendTest1(@RequestBody MessageBase<String> msg) {
        rabbitMqUtils.sendByRoutingKey(RabbitMqConstants.EXCHANGE_NAME,
                RabbitMqConstants.TOPIC_TEST1_ROUTING_KEY_TEST, msg);
        return "发送成功！";
    }

    /**
     * 发送消息test2
     * @param msg 消息内容
     * @return 发送成功
     */
    @PostMapping(value = "/test2")
    public String sendTest2(@RequestBody MessageBase<String> msg) {
        rabbitMqUtils.sendByRoutingKey(RabbitMqConstants.EXCHANGE_NAME,
                RabbitMqConstants.TOPIC_TEST2_ROUTING_KEY_TEST, msg);
        return "发送成功！";
    }

}
