package com.huangrx.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.huangrx.json.fastjson.FastjsonUtil;
import com.huangrx.json.gson.GsonUtil;
import com.huangrx.json.jackson.JacksonUtil;
import com.huangrx.json.module.User;
import com.huangrx.json.module.User2;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * 测试类
 *
 * @author hrenxiang
 * @since 2022-05-25 9:18 AM
 */
@RestController
public class Api {

    private static final ImmutableMap<String, String> IMMUTABLE_MAP = ImmutableMap.of("sport", "basketball", "food", "watermelon");

    /**
     * 默认不包含空值
     */
    @Test
    public void fastJsonTest1() {
        User huangrx = User.builder()
                .username(null)
                .password("123456")
                .createTime(new Date())
                .birthday(LocalTime.now())
                .hobby(IMMUTABLE_MAP)
                .build();

        System.out.println(JSON.toJSONString(huangrx));
        System.out.println(FastjsonUtil.format(FastjsonUtil.to(huangrx)));
        // 结果：{"birthday":"09:23:37.836","createTime":1653441817829,"hobby":{"sport":"basketball","food":"watermelon"},"password":"123456","username":"huangrx"}
    }

    @Test
    public void fastJsonTest2() {
        String json = "{\n" +
                "\t\"birthday\":\"09:27:48.940\",\n" +
                "\t\"password\":\"123456\",\n" +
                "\t\"createTime\":1653442068933,\n" +
                "\t\"hobby\":{\n" +
                "\t\t\"sport\":\"basketball\",\n" +
                "\t\t\"food\":\"watermelon\"\n" +
                "\t},\n" +
                "\t\"username\":\"huangrx\"\n" +
                "}";

        System.out.println(FastjsonUtil.from(json, User.class));
        // 结果：User(username=huangrx, password=123456, createTime=Wed May 25 09:17:34 CST 2022, birthday=09:17:34.602, hobby={sport=basketball, food=watermelon})
    }

    /**
     * 从上述 序列化和发序列化 测试结果来看，localtime 以及 date 类型的转换与我们预期不符
     * 下面 我们从 fastjson的纬度解决一下
     *
     * 第一种方式：在User实体中增加相应json格式化注解
     * 第二种方式：使用全局设置，改变fastJson 格式化时默认的写日期
     */
    @Test
    public void fastJsonTest3() {
        User2 huangrx = User2.builder()
                .username("huangrx")
                .password("123456")
                .createTime(new Date())
                .birthday(LocalDateTime.now())
                .hobby(IMMUTABLE_MAP)
                .build();

        System.out.println(FastjsonUtil.format(FastjsonUtil.to(huangrx)));
    }

    /**
     * 第二种方式：使用全局设置，改变fastJson 格式化时默认的写日期
     */
    @PostMapping("get")
    public String fastJsonTest4() throws JsonProcessingException {
        User2 huangrx = User2.builder()
                .username(null)
                .password("123456")
                .createTime(new Date())
                .birthday(LocalDateTime.now())
                .hobby(IMMUTABLE_MAP)
                .build();
        System.out.println(huangrx);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(huangrx);
    }

    /**
     * 反序列化
     */
    @Test
    public void fastJsonTest5() {
        String json = "{\n" +
                "\t\"birthday\":\"09:27:48.940\",\n" +
                "\t\"password\":\"123456\",\n" +
                "\t\"createTime\":1653442068933,\n" +
                "\t\"hobby\":{\n" +
                "\t\t\"sport\":\"basketball\",\n" +
                "\t\t\"food\":\"watermelon\"\n" +
                "\t},\n" +
                "\t\"username\":\"huangrx\"\n" +
                "}";

        User user = FastjsonUtil.from(json, new TypeReference<User>(){});
        System.out.println(user);
        // 结果：User(username=huangrx, password=123456, createTime=Wed May 25 09:17:34 CST 2022, birthday=09:17:34.602, hobby={sport=basketball, food=watermelon})
    }

    /**
     * jackson 序列化，如果有空，默认为空
     */
    @Test
    public void jacksonTest1() throws JsonProcessingException {
        User2 huangrx = User2.builder()
                .username(null)
                .password("123456")
                .createTime(new Date())
                .birthday(LocalDateTime.now())
                .hobby(IMMUTABLE_MAP)
                .build();

        String format = JacksonUtil.format(JacksonUtil.to(huangrx));
        System.out.println(format);
        // 结果：{"birthday":"09:23:37.836","createTime":1653441817829,"hobby":{"sport":"basketball","food":"watermelon"},"password":"123456","username":"huangrx"}

        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(huangrx);
        System.out.println(s);
        // 结果：{"username":null,"password":"123456","createTime":1654138346452,"birthday":{"month":"JUNE","year":2022,"dayOfMonth":2,"hour":10,"minute":52,"monthValue":6,"nano":459000000,"second":26,"dayOfWeek":"THURSDAY","dayOfYear":153,"chronology":{"id":"ISO","calendarType":"iso8601"}},"hobby":{"sport":"basketball","food":"watermelon"}}

        String json = "{\n" +
                "  \"password\" : \"123456\",\n" +
                "  \"createTime\" : \"2022-06-02 10:58:29\",\n" +
                "  \"birthday\" : \"2022-06-02 10:58:29\",\n" +
                "  \"hobby\" : {\n" +
                "    \"sport\" : \"basketball\",\n" +
                "    \"food\" : \"watermelon\"\n" +
                "  }\n" +
                "}";

        User user = JacksonUtil.from(json, User.class);
        System.out.println(user);
    }

    @Test
    public void gsonTest1() {
        User2 huangrx = User2.builder()
                .username(null)
                .password("123456")
                .createTime(new Date())
                .birthday(LocalDateTime.now())
                .hobby(IMMUTABLE_MAP)
                .build();

        System.out.println(GsonUtil.to(huangrx));
        // 结果：{"password":"123456","createTime":"2022-05-25 14:11:58","birthday":{"date":{"year":2022,"month":5,"day":25},"time":{"hour":14,"minute":11,"second":58,"nano":47000000}},"hobby":{"sport":"basketball","food":"watermelon"}}
    }
}
