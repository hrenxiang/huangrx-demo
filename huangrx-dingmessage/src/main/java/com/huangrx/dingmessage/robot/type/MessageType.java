package com.huangrx.dingmessage.robot.type;

import org.springframework.web.servlet.mvc.method.annotation.PathVariableMapMethodArgumentResolver;

/**
 * 定义消息类型，目前有文本、链接、MarkDown、跳转卡片、消息卡片五种枚举值
 *
 * @author    hrenxiang
 * @since     2022/6/27 12:56
 */
public enum MessageType {

    /**
     * 文本类型
     */
    text,

    /**
     * 链接类型
     */
    link,

    /**
     * MarkDown类型
     */
    markdown,

    /**
     * 跳转卡片类型
     */
    actionCard,

    /**
     * 消息卡片类型
     */
    feedCard;

}
