package com.huangrx.dingmessage.robot.entity;

import com.huangrx.dingmessage.robot.type.MessageType;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息卡片类型Message
 *
 * @author    hrenxiang
 * @since     2022/6/27 12:51
 */
public class FeedCardMessage extends BaseMessage {

    private static final long serialVersionUID = 1328867346902080211L;
    private static final int MAX_BUTTON_COUNT = 10;
    private static final int MIN_BUTTON_COUNT = 1;

    /**
     * 消息明细条目
     */
    private List<FeedCardMessageItem> feedCardItems = new ArrayList<>();

    public FeedCardMessage() {
        super();
    }

    public FeedCardMessage(List<FeedCardMessageItem> feedCardItems) {
        if (!(feedCardItems instanceof ArrayList)) {
            throw new IllegalArgumentException("feedCardItems must bu ArrayList");
        }
        if (feedCardItems.size() > MAX_BUTTON_COUNT) {
            throw new IllegalArgumentException("the number of buttons is not advise bigger than " + MAX_BUTTON_COUNT);
        }
        this.feedCardItems = feedCardItems;
    }

    @Override
    protected void init() {
        this.msgtype = MessageType.feedCard;
    }

    @Override
    public Map toMessageMap() {

        if (feedCardItems == null || feedCardItems.size() < MIN_BUTTON_COUNT) {
            throw new IllegalArgumentException("the number of feedCardItems is not allow lower than " + MIN_BUTTON_COUNT);
        }

        if (feedCardItems.size() > MAX_BUTTON_COUNT) {
            throw new IllegalArgumentException("the number of buttons is not advise bigger than " + MAX_BUTTON_COUNT);
        }

        HashMap<String, Object> resultMap = new HashMap<>(8);
        resultMap.put("msgtype", this.msgtype);

        HashMap<String, Object> feedCardMap = new HashMap<>(8);
        feedCardMap.put("links", this.feedCardItems);
        resultMap.put("feedCard", feedCardMap);

        return resultMap;
    }

    public List<FeedCardMessageItem> getFeedCardItems() {
        return feedCardItems;
    }

    public void addFeedCardItem(FeedCardMessageItem item) {
        if (item == null || StringUtils.isEmpty(item.getMessageURL()) ||
                StringUtils.isEmpty(item.getPicURL()) || StringUtils.isEmpty(item.getTitle())) {
            throw new IllegalArgumentException("please check the necessary parameters of item!");
        }
        if (feedCardItems == null || feedCardItems.size() >= MAX_BUTTON_COUNT) {
            throw new IllegalArgumentException("the number of buttons is not advise bigger than " + MAX_BUTTON_COUNT);
        }
        feedCardItems.add(item);
    }
}
