package com.huangrx.json.module;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.Format;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
public class User2 {

    private String username;

    private String password;

    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    private LocalDateTime birthday;

    private Map<String, String> hobby;
}
