package com.huangrx.dingmessage.robot.entity;

/**
 * 消息卡片中的明细条目
 *
 * <a href="https://open.dingtalk.com/document/robots/message-types-and-data-format"/>
 *
 * @author    hrenxiang
 * @since     2022/6/27 13:32
 */
public class FeedCardMessageItem {

    /**
     * 标题
     */
    private String title;

    /**
     * 消息跳转URL
     */
    private String messageURL;

    /**
     * 封面图片URL
     */
    private String picURL;

    public FeedCardMessageItem() {
    }

    public FeedCardMessageItem(String title, String messageURL, String picURL) {
        this.title = title;
        this.messageURL = messageURL;
        this.picURL = picURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessageURL() {
        return messageURL;
    }

    public void setMessageURL(String messageURL) {
        this.messageURL = messageURL;
    }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }
}