package com.huangrx.hutool;

import cn.hutool.core.date.*;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间工具类的使用<br/><br/>
 * <p>
 * DateUtil 针对日期时间操作提供一系列静态方法<br/><br/>
 * DateTime 提供类似于Joda-Time中日期时间对象的封装，继承自Date类，并提供更加丰富的对象方法。<br/><br/>
 * FastDateFormat 提供线程安全的针对Date对象的格式化和日期字符串解析支持。此对象在实际使用中并不需要感知，相关操作已经封装在DateUtil和DateTime的相关方法中。<br/><br/>
 * DateBetween 计算两个时间间隔的类，除了通过构造新对象使用外，相关操作也已封装在DateUtil和DateTime的相关方法中。<br/><br/>
 * TimeInterval 一个简单的计时器类，常用于计算某段代码的执行时间，提供包括毫秒、秒、分、时、天、周等各种单位的花费时长计算，对象的静态构造已封装在DateUtil中。<br/><br/>
 * DatePattern 提供常用的日期格式化模式，包括String类型和FastDateFormat两种类型。<br/><br/><br/><br/>
 * <p>
 * 日期枚举<br/><br/>
 * 与Calendar对应的这些枚举包括：<br/><br/>
 * Month 表示月份，与Calendar中的int值一一对应。<br/><br/>
 * Week 表示周，与Calendar中的int值一一对应<br/><br/>
 *
 * @author hrenxiang
 * @since 2022-05-16 5:16 PM
 */
@Slf4j
@SpringBootTest
public class DateUtilApiTest {

    /**
     * 月份枚举
     */
    @Test
    public void monthEnum() {
        int lastDay = Month.of(Calendar.JANUARY).getLastDay(false);
        log.info("lastDay: {}", lastDay);
    }

    /**
     * 时间枚举<br/><br/>
     * <p>
     * 获取分钟的毫秒数
     */
    @Test
    public void dateUnitEnum() {
        long minutesMillis = DateUnit.of(ChronoUnit.MINUTES).getMillis();
        log.info("minutesMillis: {}", minutesMillis);
    }

    /**
     * 转换<br/><br/>
     * <p>
     * Date、long、Calendar之间的相互转换
     */
    @Test
    public void convertDate() {
        //当前时间
        Date currentDate = DateUtil.date();
        //当前时间
        Date currentDate2 = DateUtil.date(Calendar.getInstance());
        //当前时间
        Date currentDate3 = DateUtil.date(System.currentTimeMillis());
        //当前时间字符串，格式：yyyy-MM-dd HH:mm:ss
        String currentDateStr = DateUtil.now();
        //当前日期字符串，格式：yyyy-MM-dd
        String today = DateUtil.today();

        log.info("currentDate: {}, currentDate2: {}, currentDate3: {}, currentDateStr: {}, " +
                "today: {}", currentDate, currentDate2, currentDate3, currentDateStr, today);

    }

    /**
     * 字符串转日期
     */
    @Test
    public void strToDate() {
        String currentDateStr = "2022-05-16 17:37:09";
        DateTime date = DateUtil.date(1652693829L * 1000);

        DateTime dateTime = DateUtil.parse(currentDateStr, DatePattern.NORM_DATETIME_PATTERN);
        String dateTime2 = DateUtil.format(date, DatePattern.NORM_DATETIME_PATTERN);

        log.info("dateTime: {}, date: {}, dateTime2: {}", dateTime, date, dateTime2);
    }

    /**
     * 获取Date对象的某个部分
     */
    @Test
    public void datePart() {
        DateTime currentDate = DateUtil.date();
        //获得年的部分
        int year = DateUtil.year(currentDate);
        //获得月份，从0开始计数
        int month = DateUtil.month(currentDate);
        //获得月份枚举
        Month monthEnum = DateUtil.monthEnum(currentDate);

        log.info("year: {}, month: {}, monthEnum: {}", year, month + 1, monthEnum);
    }

    /**
     * 开始和结束时间
     */
    @Test
    public void beginAndEndDate() {
        String dateStr = "2022-05-16 17:37:09";
        Date date = DateUtil.parse(dateStr);

        //一天的开始，结果：2022-05-16 00:00:00
        Date beginOfDay = DateUtil.beginOfDay(date);

        //一天的结束，结果：2022-05-16 23:59:59
        Date endOfDay = DateUtil.endOfDay(date);

        log.info("beginOfDay: {}, endOfDay: {}", beginOfDay, endOfDay);
    }

    /**
     * 日期时间偏移<br/><br/>
     *
     * 日期或时间的偏移指针对某个日期增加或减少分、小时、天等等，达到日期变更的目的
     */
    @Test
    public void offsetDate() {
        String dateStr = "2022-05-16 17:37:09";
        Date date = DateUtil.parse(dateStr);

        //结果：2022-05-18 17:37:09
        Date newDate = DateUtil.offset(date, DateField.DAY_OF_MONTH, 2);

        //常用偏移，结果：2022-05-19 17:37:09
        DateTime newDate2 = DateUtil.offsetDay(date, 3);

        //常用偏移，结果：2022-05-16 14:37:09
        DateTime newDate3 = DateUtil.offsetHour(date, -3);

        log.info("newDate: {}, newDate2: {}, newDate3: {}", newDate, newDate2, newDate3);
    }

    /**
     * 日期时间差
     */
    @Test
    public void dateTimeDiff() {
        String dateStr1 = "2022-05-16 17:37:09";
        Date date1 = DateUtil.parse(dateStr1);

        String dateStr2 = "2022-06-16 17:37:09";
        Date date2 = DateUtil.parse(dateStr2);

        //相差一个月，31天
        long betweenDay = DateUtil.between(date1, date2, DateUnit.DAY);

        log.info("betweenDay: {}", betweenDay);
    }

    /**
     * 格式化时间差 <br/><br/>
     * 有时候我们希望看到易读的时间差，比如XX天XX小时XX分XX秒，此时使用DateUtil.formatBetween方法
     */
    @Test
    public void formatDateDiff() {
        DateTime date = DateUtil.date();
        String dateStr1 = "2022-05-16 17:37:09";
        Date date1 = DateUtil.parse(dateStr1);

        //Level.MINUTE表示精确到分
        String formatBetween = DateUtil.formatBetween(date, date1, BetweenFormatter.Level.MILLISECOND);
        //输出：31天1小时
        log.info("formatBetween: {}", formatBetween);
    }

    /**
     * 星座和属相
     */
    @Test
    public void zodiac() {
        // "摩羯座"
        String zodiac = DateUtil.getZodiac(Month.NOVEMBER.getValue(), 14);

        // "兔"
        String chineseZodiac = DateUtil.getChineseZodiac(1999);

        log.info("zodiac: {}, chineseZodiac: {}", zodiac, chineseZodiac);
    }

    /**
     * 其他
     */
    @Test
    public void other() {
        //年龄
        int age = DateUtil.ageOfNow("1999-10-14");

        //是否闰年
        boolean leapYear = DateUtil.isLeapYear(2022);

        log.info("age: {}, leapYear: {}", age, leapYear);
    }

    /**
     * dateTime 新建对象
     */
    @Test
    public void dateTime() {
        Date date = new Date();

        //new方式创建
        DateTime time = new DateTime(date);

        //of方式创建
        DateTime now = DateTime.now();
        DateTime dt = DateTime.of(date);

        log.info("time: {}, now: {}, dt: {}", time, now, dt);
    }

    /**
     * 使用对象
     */
    @Test
    public void useDateTime() {
        DateTime dateTime = new DateTime(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN), DatePattern.NORM_DATETIME_FORMAT);

        //年，结果：2022
        int year = dateTime.year();

        //季度 Q2 --- 2
        Quarter quarter = dateTime.quarterEnum();

        //月份，结果：MAY
        Month month = dateTime.monthEnum();

        //日，结果：17
        int day = dateTime.dayOfMonth();

        log.info("year: {}, quarter: {}, month: {}, day: {}", year, quarter.getValue(), month, day);
    }

    /**
     * 对象的可变性
     */
    @Test
    public void offset() {
        DateTime dateTime = new DateTime(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN), DatePattern.NORM_DATETIME_FORMAT);

        //默认情况下DateTime为可变对象，此时offset == dateTime
        DateTime offset = dateTime.offset(DateField.YEAR, 0);

        //设置为不可变对象后变动将返回新对象，此时offset != dateTime
        dateTime.setMutable(false);
        DateTime offset2 = dateTime.offset(DateField.YEAR, 0);

        //offset: 2022-05-17 11:36:24, offset2: 2022-05-17 11:36:24
        log.info("offset: {}, offset2: {}", offset, offset2);
        //true,false
        log.info("{},{}", offset == dateTime, offset2 == dateTime);
    }

    /**
     * 农历日期
     */
    @Test
    public void chineseDateTime() {
        //通过农历构建
        ChineseDate chineseDate = new ChineseDate(2022,05,17);

        //通过公历构建
        ChineseDate chineseDate2 = new ChineseDate(DateUtil.parseDate("2022-05-17"));

        //chineseDate: 壬寅虎年 五月十七, chineseDate2: 壬寅虎年 四月十七
        log.info("chineseDate: {}, chineseDate2: {}", chineseDate, chineseDate2);

        //通过公历构建
        ChineseDate date = new ChineseDate(DateUtil.parseDate("2022-05-17"));
        // 一月
        String chineseMonth = date.getChineseMonth();
        // 正月
        String chineseMonthName = date.getChineseMonthName();
        // 初一
        String chineseDay = date.getChineseDay();
        // 庚子
        String cyclical = date.getCyclical();
        // 生肖：鼠
        String chineseZodiac = date.getChineseZodiac();
        // 传统节日（部分支持，逗号分隔）：春节
        String festivals = date.getFestivals();
        // 庚子鼠年 正月初一
        String s = date.toString();
        log.info("{}", chineseMonth);
        log.info("{}", chineseMonthName);
        log.info("{}", chineseDay);
        log.info("{}", cyclical);
        log.info("{}", chineseZodiac);
        log.info("{}", festivals);
        log.info("{}", s);
    }

    /**
     * localDateTime
     */
    @Test
    public void localDateTime() {
        //ISO时间
        String dateStr = "2020-01-23T12:23:56";
        DateTime dt = DateUtil.parse(dateStr);
        log.info("{}", dt);

        // Date对象转换为LocalDateTime
        LocalDateTime of = LocalDateTimeUtil.of(dt);
        log.info("{}", of);

        // 时间戳转换为LocalDateTime
        of = LocalDateTimeUtil.ofUTC(dt.getTime());
        log.info("{}, {}",dt.getTime(), of);
    }

    /**
     * timer
     */
    @Test
    public void timer() {
        final TimeInterval timer = new TimeInterval();

        // 分组1
        timer.start("1");
        ThreadUtil.sleep(800);

        // 分组2
        timer.start("2");
        ThreadUtil.sleep(900);

        log.info("Timer 1 took {} ms", timer.intervalMs("1"));
        log.info("Timer 2 took {} ms", timer.intervalMs("2"));
    }
}
