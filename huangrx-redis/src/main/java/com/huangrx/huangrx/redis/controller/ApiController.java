package com.huangrx.huangrx.redis.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huangrx.huangrx.redis.model.entity.Person;
import com.huangrx.huangrx.redis.service.RedisService;
import com.huangrx.huangrx.redis.util.JacksonUtil;
import com.huangrx.huangrx.redis.util.ObjectValCheck;
import com.huangrx.huangrx.redis.model.vo.Demo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.huangrx.huangrx.redis.service.CacheConstants.TEST_KEY;
import static com.huangrx.huangrx.redis.service.CacheConstants.generateKey;

/**
 * 整合 redis 缓存 测试
 *
 * @author hrenxiang
 * @since 2022-04-24 9:57 PM
 */
@RestController
public class ApiController {

    private final RedisService redisService;

    public ApiController(RedisService redisService) {
        this.redisService = redisService;
    }

    @RequestMapping(value = "/set", method = RequestMethod.GET)
    public String set() {
        String key = generateKey(TEST_KEY, 3, 4);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value1", "h");
        jsonObject.put("value2", "r");
        jsonObject.put("isBoy", 1);
        redisService.set(key,jsonObject.toString());
        return "添加缓存成功！！！";
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String get() {
        String key = generateKey(TEST_KEY, 1, 2);
        return "获取缓存成功！！！" + JSON.parseObject(redisService.get(key).toString(), Demo.class);
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public String del() {
        String key = generateKey(TEST_KEY, 1, 2);
        Boolean del = redisService.del(key);
        return ObjectValCheck.isTrue(del) ? "缓存删除成功！！！" : "缓存删除失败！！！";
    }

    private static final List<Person> personList = new ArrayList<Person>();

    static {
        Person john = Person.builder()
                .name("John")
                .email("john@example.com")
                .pid("111")
                .build();
        personList.add(john);

        Person alisa = Person.builder()
                .name("alisa")
                .email("alisa@example.com")
                .pid("333")
                .build();
        personList.add(alisa);

        Person nn = Person.builder()
                .name("nn")
                .email("nn@example.com")
                .pid("444")
                .build();
        personList.add(nn);

        Person vv = Person.builder()
                .name("xx")
                .email("xx@example.com")
                .pid("555")
                .build();
        personList.add(vv);

        Person bb = Person.builder()
                .name("bb")
                .email("bb@example.com")
                .pid("666")
                .build();
        personList.add(bb);

        Person dd = Person.builder()
                .name("dd")
                .email("dd@example.com")
                .pid("777")
                .build();
        personList.add(dd);

        Person ww = Person.builder()
                .name("ww")
                .email("ww@example.com")
                .pid("888")
                .build();
        personList.add(ww);

        Person hh = Person.builder()
                .name("hh")
                .email("hh@example.com")
                .pid("999")
                .build();
        personList.add(hh);
    }

    @RequestMapping(value = "/setPerson", method = RequestMethod.GET)
    public String setPerson() {
        String key = generateKey(TEST_KEY, "persons");
        redisService.set(key, JacksonUtil.to(personList));
        return "添加缓存成功！！！";
    }


}
