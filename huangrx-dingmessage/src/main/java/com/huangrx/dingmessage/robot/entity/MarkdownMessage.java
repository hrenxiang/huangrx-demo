package com.huangrx.dingmessage.robot.entity;

import com.huangrx.dingmessage.robot.type.MessageType;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Markdown消息类型
 *
 * @author    hrenxiang
 * @since     2022/6/27 12:49
 */
public class MarkdownMessage extends BaseMessage {

    private static final long serialVersionUID = 8249153912223862281L;

    /**
     * 消息简介
     */
    private String text;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 可以通过群成员的绑定手机号来艾特具体的群成员
     */
    private String[] atMobiles;

    /**
     * 是否艾特所有人
     * 也可以设置isAtAll=true来艾特所有人
     */
    private boolean isAtAll;

    public MarkdownMessage() {
    }

    public MarkdownMessage(String title, String text) {
        this.text = text;
        this.title = title;
    }

    public MarkdownMessage(String title, String text, String[] atMobiles) {
        this.text = text;
        this.title = title;
        this.atMobiles = atMobiles;
    }

    public MarkdownMessage(String title, String text, boolean isAtAll) {
        this.text = text;
        this.title = title;
        this.isAtAll = isAtAll;
    }

    @Override
    public Map toMessageMap() {

        if (StringUtils.isEmpty(this.title) || StringUtils.isEmpty(this.text) ||
                !MessageType.markdown.equals(msgtype)) {
            throw new IllegalArgumentException("please check the necessary parameters!");
        }

        HashMap<String, Object> resultMap = new HashMap<>(8);
        resultMap.put("msgtype", this.msgtype);

        HashMap<String, String> markdownItems = new HashMap<>(8);
        markdownItems.put("title", this.title);
        markdownItems.put("text", this.text);
        resultMap.put("markdown", markdownItems);

        HashMap<String, Object> atItems = new HashMap<>(8);
        atItems.put("atMobiles", this.atMobiles);
        atItems.put("isAtAll", this.isAtAll);
        resultMap.put("at", atItems);

        return resultMap;
    }

    @Override
    protected void init() {
        this.msgtype = MessageType.markdown;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getAtMobiles() {
        return atMobiles;
    }

    public void setAtMobiles(String[] atMobiles) {
        this.atMobiles = atMobiles;
    }

    public boolean getIsAtAll() {
        return isAtAll;
    }

    public void setIsAtAll(boolean isAtAll) {
        this.isAtAll = isAtAll;
    }
}
