package com.huangrx.rocket.producer;

import com.huangrx.rocket.producer.mq.MessageBase;
import com.huangrx.rocket.producer.mq.MqConstant;
import com.huangrx.rocket.producer.mq.RocketMqUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class RocketProducerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Resource
    RocketMqUtil<String> rocketMqUtil;

    @Test
    void test() {
        MessageBase<String> messageBase = new MessageBase<>();
        messageBase.setTopic(MqConstant.Topic.HUANGRX_TOPIC);
        messageBase.setMessage("huangrx hello");
        messageBase.setTag(MqConstant.Tag.HUANGRX_TAG_INSERT);
        rocketMqUtil.sendOneWayMsg(messageBase);
        System.out.println();
    }

}
