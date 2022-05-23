package com.huangrx.rocket.producer.mq;

/**
 * @author    hrenxiang
 * @since     2022/5/23 4:59 PM
 */
public class MqConstant {

    public static final String HUANGRX_PRODUCER_GROUP = "HUANGRX_PRODUCER_GROUP";
    public static final String NAME_SERVER_ADDR = "NAME_SERVER_ADDR";
    public static final String SEND_MSG_TIMEOUT = "SEND_MSG_TIMEOUT";
    public static final String FAILURE_RETRY_NUMBER = "FAILURE_RETRY_NUMBER";

    /**
     * top
     */
    public static class Topic {
        /**
         * 稿件录入
         */
        public static final String HUANGRX_TOPIC = "HUANGRX_TOPIC";

    }

    /**
     * TAG
     */
    public static class Tag {
        public static final String HUANGRX_TAG_INSERT = "HUANGRX_TAG_INSERT";
        public static final String HUANGRX_TAG_UPDATE = "HUANGRX_TAG_UPDATE";
        public static final String HUANGRX_TAG_DELETE = "HUANGRX_TAG_DELETE";
    }

    /**
     * consumeGroup 消费者
     */
    public static class ConsumeGroup {
        public static final String HUANGRX_CONSUMER_GROUP = "HUANGRX_CONSUMER_GROUP";
    }

}