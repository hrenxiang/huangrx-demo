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
     * simple.acknowledge-mode: manual # 开启手动确认
     * getDeliveryTag: 与concurrency: 2 # 同一个队列启动几个消费者相关，如果设置两个，那两个队列getDeliveryTag都是从1开始
     * getRedelivered: 消息是否重新被消费过，如果重新消费过还要消费，则直接拒绝处理消息
     * basicReject: 拒绝消息，和basicNack相似，但没有批处理的参数，第二个参数false表明，直接丢弃，true表示接着放回队列
     * basicNack: 拒绝消息，第二个参数代表是否批处理DeliveryTag之前的数据，true表示处理之前的，false表示只处理当前值的
     *                     第三个参数代表是否放回队列
     * @param message 封装的消息内容
     */
    @RabbitListener(queues = {RabbitMqConstants.TEST1_QUEUE})
    public void process(Message message, Channel channel) throws IOException {
        try {
            log.info("received --- {}", message);
            log.info("received --- {}", new String(message.getBody(), StandardCharsets.UTF_8));

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        }  catch (Exception e) {

            if (message.getMessageProperties().getRedelivered()) {

                log.error("The message has been processed repeatedly and failed to be received again.");

                // 拒绝消息
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            } else {

                log.error("The message is about to return to the queue again for processing.");

                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
        }

    }


    /**
     * 延迟队列绑定的死信交换机，监听死信队列中的数据，延迟队列中的消息10秒未消费，则放入死信交换机，发送给死信队列
     *
     * 【需要注释掉下面的 test3，或者在test3里打断点】
     *
     * @param msg 消息内容
     * @param channel 信道
     * @param message 消息
     * @throws IOException 异常
     */
    @RabbitListener(queues = RabbitMqConstants.SPRING_DEAD_QUEUE)
    public void test2(String msg, Channel channel, Message message) throws IOException {
        try {
            //注释掉 test3，测试时这里打断点 等待10秒
            log.info("监听延迟队列消息到死信交换机中：{}", msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
            //是否已经重试过
            if (message.getMessageProperties().getRedelivered()){
                //已经重试过，直接拒绝
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
            }else {
                //未重试过，直接丢弃
                channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);
            }
        }
    }

    /**
     * 监听延迟队列中的消息
     *
     * 【订单超过30分钟未支付】
     *
     * @param msg the message
     * @param channel 信道
     * @param message 消息
     */
    @RabbitListener(queues = RabbitMqConstants.SPRING_DELAY_QUEUE)
    public void test3(String msg, Channel channel, Message message) throws IOException {
        try {
            log.info("监听延迟队列消息：{}", msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
            //是否已经重试过
            if (message.getMessageProperties().getRedelivered()){
                //已经重试过，直接拒绝
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
            }else {
                //未重试过，直接丢弃
                channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);
            }
        }
    }
}
