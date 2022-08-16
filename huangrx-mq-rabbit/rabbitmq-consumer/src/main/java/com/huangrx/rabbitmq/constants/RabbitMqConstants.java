package com.huangrx.rabbitmq.constants;

import javax.swing.*;

/**
 * RabbitMqConstants
 *
 * @author    hrenxiang
 * @since    2022/4/28 11:15 AM
 */
public class RabbitMqConstants {

    public final static String TEST1_QUEUE = "test1-queue";

    public final static String TEST2_QUEUE = "test2-queue";

    public final static String EXCHANGE_NAME = "test.topic.exchange";

    public final static String TOPIC_TEST1_ROUTING_KEY = "topic.test1.*";

    public final static String TOPIC_TEST1_ROUTING_KEY_TEST = "topic.test1.test";

    public final static String TOPIC_TEST2_ROUTING_KEY = "topic.test2.*";

    public final static String TOPIC_TEST2_ROUTING_KEY_TEST = "topic.test2.test";

    public final static String DELAY_EXCHANGE_NAME = "spring-delay-exchange";

    public final static String DEAD_EXCHANGE_NAME = "spring-dead-exchange";

    public final static String SPRING_DELAY_QUEUE = "spring-delay-queue";

    public final static String SPRING_DEAD_QUEUE = "spring-dead-queue";

    public final static String TOPIC_SPRING_DELAY_ROUTING_KEY = "ab.delay";

    public final static String TOPIC_SPRING_DEAD_ROUTING_KEY = "ab.dead";
}
