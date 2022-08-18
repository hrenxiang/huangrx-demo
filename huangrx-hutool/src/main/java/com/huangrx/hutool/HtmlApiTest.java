package com.huangrx.hutool;

import cn.hutool.http.HTMLFilter;
import cn.hutool.http.HtmlUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * html 标签过滤
 *
 * @author hrenxiang
 * @since 2022-08-18 16:35
 */
@SpringBootTest
public class HtmlApiTest {

    @Test
    void test() {
        System.out.println(new HTMLFilter(true).filter("<script>alert('XSS');</script>"));
    }
}
