package com.huangrx.evenlistener.translation.config;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author hrenxiang
 * @since 2022-07-21 16:58
 */
public class TestEvent extends ApplicationEvent {
    @Getter
    private Integer id;

    public TestEvent(Integer id) {
        super(id);
        this.id = id;
    }
}
