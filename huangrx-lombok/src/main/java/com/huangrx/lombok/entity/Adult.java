package com.huangrx.lombok.entity;

import lombok.Builder;
import lombok.Singular;
import lombok.ToString;

import java.util.List;

/**
 * @author hrenxiang
 */
@Builder
@ToString
public class Adult {
    private String name;
    private int age;

    @Singular
    private List<String> cds;
}
