package com.huangrx.huangrx.redis.model.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 实体
 *
 * @author hrenxiang
 * @since 2022-08-29 10:28
 */
@Data
@Builder
public class Person {
    private String pid;
    private String name;
    private String email;
}
