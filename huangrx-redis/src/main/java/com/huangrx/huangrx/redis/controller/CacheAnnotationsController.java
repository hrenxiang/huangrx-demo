package com.huangrx.huangrx.redis.controller;

import com.huangrx.huangrx.redis.config.HuangrxCache;
import com.huangrx.huangrx.redis.model.entity.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hrenxiang
 * @since 2022-09-14 10:36
 */
@RestController
public class CacheAnnotationsController {

    @HuangrxCache(prefix = "CACHE:PERSONLIST:", timeout = 14400, random = 3600, lock = "lock")
    @RequestMapping(value = "/cache/queryPersonList", method = RequestMethod.GET)
    public List<Person> queryPersonList(@RequestParam("pid") Long pid) {
        System.out.println("没有走缓存哦！！！！！！");
        List<Person> personList = new ArrayList<>();
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

        return personList;
    }
}
