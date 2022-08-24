package com.huangrx.mybatisplus.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.stereotype.Component;

/**
 * 覆盖默认的雪花算法生成规则，使用16位的雪花算法
 *
 * @author        hrenxiang
 * @since         2022-08-24 09:34:31
 */
@Component
public class CustomerIdGenerator implements IdentifierGenerator {
    @Override
    public Long nextId(Object entity) {
        // 填充自己的Id生成器
        return IdGenerator.generateId();
    }
}