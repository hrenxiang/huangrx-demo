package com.huangrx.rabbitmq.mq;

import com.huangrx.rabbitmq.constants.RabbitMqConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQConfig
 *
 * @author    hrenxiang
 * @since    2022/4/28 11:18 AM
 */
@Slf4j
@Configuration
public class RabbitMqInitConfig {

    private final CachingConnectionFactory connectionFactory;

    /**
     * 构造方法注入
     * @param connectionFactory 连接工厂
     */
    public RabbitMqInitConfig(CachingConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    /**
     * 如果需要在生产者需要消息发送后的回调，
     * 需要对rabbitTemplate设置ConfirmCallback对象，
     * 由于不同的生产者需要对应不同的ConfirmCallback，
     * 如果rabbitTemplate设置为单例bean，
     * 则所有的rabbitTemplate实际的ConfirmCallback为最后一次申明的ConfirmCallback。
     * @return rabbitTemplate
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory);
    }

    /**
     *  声明交换机
     */
    @Bean(RabbitMqConstants.EXCHANGE_NAME)
    public Exchange exchange(){
        //durable(true) 持久化，mq重启之后交换机还在
        return ExchangeBuilder.topicExchange(RabbitMqConstants.EXCHANGE_NAME).durable(true).build();
    }

    /**
     *  声明队列
     *  new Queue(QUEUE_EMAIL,true,false,false)
     *  durable="true" 持久化 rabbitmq重启的时候不需要创建新的队列
     *  auto-delete 表示消息队列没有在使用时将被自动删除 默认是false
     *  exclusive  表示该消息队列是否只在当前connection生效,默认是false
     */
    @Bean(RabbitMqConstants.TEST1_QUEUE)
    public Queue esQueue() {
        return new Queue(RabbitMqConstants.TEST1_QUEUE);
    }

    /**
     *  声明队列
     */
    @Bean(RabbitMqConstants.TEST2_QUEUE)
    public Queue gitalkQueue() {
        return new Queue(RabbitMqConstants.TEST2_QUEUE);
    }

    /**
     *  TEST1_QUEUE队列绑定交换机，指定routingKey
     */
    @Bean
    public Binding bindingEs(@Qualifier(RabbitMqConstants.TEST1_QUEUE) Queue queue,
                             @Qualifier(RabbitMqConstants.EXCHANGE_NAME) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMqConstants.TOPIC_TEST1_ROUTING_KEY).noargs();
    }

    /**
     *  TEST2_QUEUE队列绑定交换机，指定routingKey
     */
    @Bean
    public Binding bindingGitalk(@Qualifier(RabbitMqConstants.TEST2_QUEUE) Queue queue,
                                 @Qualifier(RabbitMqConstants.EXCHANGE_NAME) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMqConstants.TOPIC_TEST2_ROUTING_KEY).noargs();
    }

    /**
     * 延迟交换机
     * @return 通配符交换机
     */
    @Bean(RabbitMqConstants.DELAY_EXCHANGE_NAME)
    public TopicExchange topicExchange(){
        //return new TopicExchange("spring-delay-exchange",true,false);
        return ExchangeBuilder.topicExchange(RabbitMqConstants.DELAY_EXCHANGE_NAME).build();
    }

    /**
     * 延迟队列
     * @return 延迟队列
     */
    @Bean(RabbitMqConstants.SPRING_DELAY_QUEUE)
    public Queue queue(){
        Map<String,Object> arguments = new HashMap<>(5);
        arguments.put("x-message-ttl",10000);
        arguments.put("x-dead-letter-exchange",RabbitMqConstants.DEAD_EXCHANGE_NAME);
        arguments.put("x-dead-letter-rk",RabbitMqConstants.TOPIC_SPRING_DEAD_ROUTING_KEY);
        return new Queue(RabbitMqConstants.DELAY_EXCHANGE_NAME,true,false,false,arguments);
//        return QueueBuilder.durable("spring-delay-queue")
//                .withArgument("x-message-ttl", 10000)
//                .withArgument("x-dead-letter-exchange", "spring-dead-exchange")
//                .withArgument("x-dead-letter-routing-key", "ab.dead")
//                .build();
    }

    @Bean
    public Binding binding(@Qualifier(RabbitMqConstants.DELAY_EXCHANGE_NAME) TopicExchange topicExchange,
                           @Qualifier(RabbitMqConstants.SPRING_DELAY_QUEUE) Queue queue){
        return BindingBuilder.bind(queue).to(topicExchange).with(RabbitMqConstants.TOPIC_SPRING_DELAY_ROUTING_KEY);
    }

    /**
     * 死信交换机
     * @return 通配符交换机
     */
    @Bean(RabbitMqConstants.DEAD_EXCHANGE_NAME)
    public TopicExchange deadExchange(){
        return ExchangeBuilder.topicExchange(RabbitMqConstants.DEAD_EXCHANGE_NAME).build();
    }

    /**
     * 死信队列
     * @return 死信队列
     */
    @Bean(RabbitMqConstants.SPRING_DEAD_QUEUE)
    public Queue deadQueue(){
        return QueueBuilder.durable(RabbitMqConstants.SPRING_DEAD_QUEUE).build();
    }

    @Bean
    public Binding deadBinding(@Qualifier(RabbitMqConstants.DEAD_EXCHANGE_NAME) TopicExchange deadExchange,
                               @Qualifier(RabbitMqConstants.SPRING_DEAD_QUEUE) Queue deadQueue){
        return BindingBuilder.bind(deadQueue).to(deadExchange).with(RabbitMqConstants.TOPIC_SPRING_DEAD_ROUTING_KEY);
    }

}
