package com.huangrx.dingmessage.official.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

public class DingTalkEvent extends ApplicationEvent {

    private static final long serialVersionUID = -5105204374136563482L;

    @Getter
    private final String source;

    @Getter
    private final String url;

    @Getter
    @Setter
    private String secret;

    @Getter
    @Setter
    private DingTalkMessageType msgType;

    @Getter
    @Setter
    private String at;

    @Getter
    @Setter
    private Boolean secretEnable;

    @Getter
    @Setter
    private String accessToken;

    public DingTalkEvent(String source, String url) {
        super(source);
        this.source = source;
        this.url = url;
    }

    public DingTalkEvent(String source, String url, String secret, DingTalkMessageType msgType, String at, Boolean secretEnable, String accessToken) {
        super(source);
        this.source = source;
        this.url = url;
        this.secret = secret;
        this.msgType = msgType;
        this.at = at;
        this.secretEnable = secretEnable;
        this.accessToken = accessToken;
    }

}
