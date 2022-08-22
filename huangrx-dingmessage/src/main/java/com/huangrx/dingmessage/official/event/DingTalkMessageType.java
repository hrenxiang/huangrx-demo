package com.huangrx.dingmessage.official.event;

import lombok.Getter;

/**
 * 定义消息类型，目前有文本、链接、MarkDown、跳转卡片、消息卡片五种枚举值
 *
 * @author    hrenxiang
 * @since     2022/6/27 12:56
 */
public enum DingTalkMessageType {

    /**
     * 文本类型
     */
    TEXT("text"),

    /**
     * 链接类型
     */
    LINK("link"),

    /**
     * MarkDown类型
     */
    MARKDOWN("markdown"),

    /**
     * 跳转卡片类型
     */
    ACTION_CARD("actionCard"),

    /**
     * 消息卡片类型
     */
    FEED_CARD("feedCard");

    @Getter
    private final String type;

    DingTalkMessageType(String type) {
        this.type = type;
    }

}
