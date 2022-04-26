package com.huangrx.huangrx.redis.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huangrx.huangrx.redis.service.RedisService;
import com.huangrx.huangrx.redis.util.ObjectValCheck;
import com.huangrx.huangrx.redis.vo.Demo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
