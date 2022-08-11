package com.huangrx.json.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;
import java.util.Map;

/**
 * 用户
 *
 * @author hrenxiang
 * @since 2022-05-25 9:02 AM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String username;

    private String password;

    private Date createTime;

    private LocalTime birthday;

    private Map<String, String> hobby;
}
