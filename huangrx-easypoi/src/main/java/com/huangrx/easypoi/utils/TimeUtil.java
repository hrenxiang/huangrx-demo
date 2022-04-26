package com.huangrx.easypoi.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author        hrenxiang
 * @since         2022/4/26 2:34 PM
 */
public class TimeUtil {
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String YYMMDD = "yyMMdd";

    public static final String YYYYMMDD = "yyyyMMdd";

    /**
     * 格式化日期
     *
     * @param time 当前时间
     * @return 格式化后的日期
     */
    public static String format(Date time) {
        return format(time, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 格式化日期
     *
     * @param time    当前时间
     * @param pattern 格式化规则
     * @return 格式化后的日期
     */
    public static String format(Date time, String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        ZoneId zoneId = ZoneId.systemDefault();
        return dtf.format(time.toInstant().atZone(zoneId));
    }

    /**
     * timestamp 转 字符串
     *
     * @param timestamp 时间
     * @return 转换为字符串的日期
     */
    public static String dateToStr(long timestamp) {
        return dateToStr(timestamp, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * timestamp 转 字符串
     *
     * @param timestamp 时间戳
     * @param pattern   格式化规则
     * @return 转换为字符串的日期
     */
    public static String dateToStr(long timestamp, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        ZoneId zoneId = ZoneId.systemDefault();
        return formatter.format(new Date(timestamp).toInstant().atZone(zoneId));
    }

    /**
     * 字符串 转 date
     *
     * @param time 时间字符串
     * @return 字符串转换后的日期
     */
    public static Date strToDate(String time) {
        return strToDate(time, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 字符串 转 date
     *
     * @param time    当前时间
     * @param pattern 格式化规则
     * @return 字符串转换后的日期
     */
    public static Date strToDate(String time, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDate.parse(time, formatter).atStartOfDay();
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 当前时间格式化
     *
     * @return 当前时间
     */
    public static String currentTimeFormat() {
        LocalDateTime nowDate = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
        return dtf.format(nowDate);
    }

    /**
     * 当前时间格式化
     *
     * @param pattern 格式化规则
     * @return 当前时间
     */
    public static String currentTimeFormat(String pattern) {
        LocalDateTime nowDate = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return dtf.format(nowDate);
    }
}