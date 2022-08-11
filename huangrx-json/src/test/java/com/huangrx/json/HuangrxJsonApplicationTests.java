package com.huangrx.json;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.huangrx.json.fastjson.FastjsonUtil;
import com.huangrx.json.module.User;
import com.huangrx.json.module.User2;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class HuangrxJsonApplicationTests {

    @Test
    void contextLoads() {
        ImmutableMap<String, String> immutableMap = ImmutableMap.of("sport", "basketball", "food", "watermelon");
        User2 huangrx = User2.builder()
                .username("huangrx")
                .password("123456")
                .createTime(new Date())
                .birthday(LocalDateTime.now())
                .hobby(immutableMap)
                .build();

        System.out.println(FastjsonUtil.to(huangrx));


        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("1");
        list.add("1");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    void test() {
        String json = "{\n" +
                "    \"goodsIds\": [\n" +
                "        3458\n" +
                "    ]\n" +
                "}";

        GoodsPushParam goodsPushParam = JSON.parseObject(json, GoodsPushParam.class);
        System.out.println(goodsPushParam.getGoodsIds()[0]);
    }

    @Data
    static class GoodsPushParam {

        private Integer[] goodsIds;


    }

    @Test
    void test2() {
        String msg = "{\n" +
                "    \"message\": null,\n" +
                "    \"errorMessage\": \"\",\n" +
                "    \"responseCode\": 0,\n" +
                "    \"hasError\": false,\n" +
                "    \"success\": true,\n" +
                "    \"paginationDTO\": {\n" +
                "        \"pageNumber\": 0,\n" +
                "        \"pageSize\": 0,\n" +
                "        \"totalCount\": 0\n" +
                "    },\n" +
                "    \"data\": []\n" +
                "}";
    }

}
