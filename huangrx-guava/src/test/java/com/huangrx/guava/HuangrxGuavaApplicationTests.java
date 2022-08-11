package com.huangrx.guava;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HuangrxGuavaApplicationTests {

    @Test
    void contextLoads() {
        a a = new a();
        a.setName(123);
        a.setSex(1);
        b b = new b();
        b.setAddress(12);

        BeanUtils.copyProperties(a, b);

        System.out.println(b);
    }

    @Data
    class a{
        Integer name;

        Integer sex;
    }

    @Data
    class b extends a {
        Integer address;
    }

}
