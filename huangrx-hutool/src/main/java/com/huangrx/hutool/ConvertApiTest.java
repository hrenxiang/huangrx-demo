package com.huangrx.hutool;

import cn.hutool.Hutool;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HtmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Convert类
 *
 * Convert类可以说是一个工具方法类，里面封装了针对Java常见类型的转换，用于简化类型转换。
 * Convert类中大部分方法为toXXX，参数为Object，可以实现将任意可能的类型转换为指定类型。
 * 同时支持第二个参数defaultValue用于在转换失败时返回一个默认值。
 *
 * @author hrenxiang
 * @since 2022-05-16 3:47 PM
 */
@Slf4j
@SpringBootTest
public class ConvertApiTest {

    /**
     * 转换为字符串
     */
    @Test
    public void convertToStr() {
        int a = 1;
        String intStr = Convert.toStr(a);

        long[] b = {1,2,3,4,5};
        String longStr = Convert.toStr(b);

        log.info("intStr: {}, longStr: {}", intStr, longStr);
    }

    /**
     * 转换为数组
     */
    @Test
    public void convertToArr() {
        String[] strArr = {"1","2","3","4","5","6"};
        Integer[] integers = Convert.toIntArray(strArr);

        long[] longArr = {1,2,3,4,5,6};
        Integer[] integers1 = Convert.toIntArray(longArr);

        log.info("integers: {}, integers1: {}", integers, integers1);
    }

    /**
     * 转换日期对象
     */
    @Test
    public void convertToDate() {
        String a = "2022-05-16";
        Date value = Convert.toDate(a);

        log.info("value: {}", value);
    }

    /**
     * 其它类型转换
     */
    @Test
    public void convertToOther() {
        Object[] a = { "a", "你", "好", "", 1 };
        List<String> list = Convert.convert(new TypeReference<List<String>>() {}, a);

        log.info("list: {}", list.size() + " - " + list.toString());
    }

    /**
     * 半角和全角转换
     */
    @Test
    public void convertToHalfAngle() {
        String a = "123456789";
        String sbcCase = Convert.toSBC(a);

        String a1 = "１２３４５６７８９";
        String dbcCase = Convert.toDBC(a);

        log.info("sbcCase: {}, dbcCase: {}", sbcCase, dbcCase);
    }

    /**
     * 16进制（Hex）
     */
    @Test
    public void convertToHex() {
        String a = "我是一个小小的可爱的字符串";
        String hex = Convert.toHex(a, CharsetUtil.CHARSET_UTF_8);

        //注意：在4.1.11之后hexStrToStr将改名为hexToStr
        String raw = Convert.hexToStr(hex, CharsetUtil.CHARSET_UTF_8);

        log.info("hex: {}, raw: {}", hex, raw);
    }

    /**
     * 编码转换 <br/><br/>
     *
     * 注意 经过测试，UTF-8编码后用GBK解码再用GBK编码后用UTF-8解码会存在某些中文转换失败的问题。
     */
    @Test
    public void convertToCharset() {
        String a = "我不是乱码";
        String result = Convert.convertCharset(a, CharsetUtil.UTF_8, CharsetUtil.ISO_8859_1);
        String raw = Convert.convertCharset(result, CharsetUtil.ISO_8859_1, "UTF-8");
        Assert.assertEquals(raw, a);

        log.info("result: {}, raw: {}", result, raw);
    }

    /**
     * 时间单位转换<br/><br/>
     *
     * Convert.convertTime方法主要用于转换时长单位，比如一个很大的毫秒，我想获得这个毫秒数对应多少分：
     */
    @Test
    public void convertToTimeUnit() {
        long a = 4535345;
        long minutes = Convert.convertTime(a, TimeUnit.MILLISECONDS, TimeUnit.MINUTES);

        log.info("minutes: {}", minutes);
    }

    /**
     * 金额大小写转换<br/><br/>
     *
     * Convert.digitToChinese将金钱数转换为大写形式<br/><br/>
     *
     * 注意 转换为大写只能精确到分（小数点儿后两位），之后的数字会被忽略。
     */
    @Test
    public void convertToDigitToChinese() {
        double a = 67556.32;

        String digitUppercase = Convert.digitToChinese(a);

        log.info("digitUppercase: {}", digitUppercase);
    }
}
