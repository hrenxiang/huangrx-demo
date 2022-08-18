package com.huangrx.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hrenxiang
 * @since 2022-08-18 14:46
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 8977517219373673802L;
    private String name;

    private String pwd;

}