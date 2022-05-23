package com.huangrx.rocket.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 异步发送消息
 *
 * @author hrenxiang
 * @since 2022/5/23 2:37 PM
 */
@Slf4j
public class AsyncProducer {
    public static void main(String[] args) throws Exception {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("huangrx-async-producer");
        // 设置NameServer的地址
        producer.setNamesrvAddr("192.168.2.105:9876");
        // 启动Producer实例
        producer.start();
        // 设置发送异步失败的重试次数
        producer.setRetryTimesWhenSendAsyncFailed(0);

        // 初始化消息数量
        int messageCount = 100;
        // 初始化倒计时计数器
        CountDownLatch2 countDownLatch = new CountDownLatch2(messageCount);
        for (int i = 0; i < messageCount; i++) {
            final int index = i;
            // 创建消息，并指定Topic，Tag和消息体
            Message message = new Message("TopicTest", "TagA", "huangrx-110", "Hello world".getBytes(StandardCharsets.UTF_8));
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    countDownLatch.countDown();
                    System.out.printf("%-10d OK %s %n", index,
                            sendResult.getMsgId());
                }

                @Override
                public void onException(Throwable e) {
                    countDownLatch.countDown();
                    System.out.printf("%-10d Exception %s %n", index, e);
                    e.printStackTrace();
                }
            });
        }

        // 等待五秒
        countDownLatch.await(5, TimeUnit.SECONDS);
        producer.shutdown();
    }
}