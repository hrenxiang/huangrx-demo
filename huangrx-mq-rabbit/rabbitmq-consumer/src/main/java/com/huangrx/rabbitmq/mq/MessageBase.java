package com.huangrx.rabbitmq.mq;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * 发送消息封装对象.
 *
 * @author hrenxiang
 * @since 2022/4/28 10:33 AM
 */
public class MessageBase<T> implements Serializable {


    private static final long serialVersionUID = 4340055638835144475L;
    /**
     * 发送消息的key.
     */
    private String key;

    /**
     * 发送消息的tag.
     */
    private String tag;

    /**
     * 延迟的时间.
     */
    private Long delayTime;

    /**
     * 消息内容
     */
    private T message;

    /**
     * 开始发送消息的时间
     */
    private Long startDeliverTime;

    /**
     * 消息类型
     */
    private Integer messageType;
    /**
     * topic
     */
    private String topic;
    /**
     * groupId
     */
    private String groupId;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Long delayTime) {
        this.delayTime = delayTime;

        Long startDeliverTime = System.currentTimeMillis() + delayTime;
        // 设置延迟发送消息的时间
        setStartDeliverTime(startDeliverTime);
    }

    public Long getStartDeliverTime() {
        return startDeliverTime;
    }

    /**
     * 设置定时/延时发送消息时间
     *
     * @param startDeliverTime 开始交付时间
     */
    public void setStartDeliverTime(Long startDeliverTime) {
        this.startDeliverTime = startDeliverTime;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }

    public byte[] getSerializeMessage() {
        byte[] bytes;
        bytes = message.toString().getBytes(StandardCharsets.UTF_8);
        return bytes;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "MessageBase{" +
                "key='" + key + '\'' +
                ", tag='" + tag + '\'' +
                ", delayTime=" + delayTime +
                ", message=" + message +
                ", startDeliverTime=" + startDeliverTime +
                ", messageType=" + messageType +
                ", topic='" + topic + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }
}
