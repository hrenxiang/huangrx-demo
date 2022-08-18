package com.huangrx.service.impl;

import com.huangrx.service.PersonService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author hrenxiang
 * @since 2022-08-18 15:39
 */
@DubboService
public class PersonServiceImpl implements PersonService {
    @Override
    public String sayCool() {
        return "酷的酷的！！！";
    }
}
