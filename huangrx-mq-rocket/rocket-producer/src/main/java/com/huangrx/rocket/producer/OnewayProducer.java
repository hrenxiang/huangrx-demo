package com.huangrx.rocket.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * 单向发送 不关心发送结果
 *
 * @author hrenxiang
 * @since 2022/5/23 3:39 PM
 */
public class OnewayProducer {
    public static void main(String[] args) throws Exception {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("huangrx-oneway-producer");
        // 设置NameServer的地址
        producer.setNamesrvAddr("192.168.2.105:9876");
        // 启动Producer实例
        producer.start();

        int messageCount = 100;
        for (int i = 0; i < messageCount; i++) {
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message("TopicTest",
                    "TagA", ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            // 发送单向消息，没有任何返回结果
            producer.sendOneway(msg);

        }
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }
}