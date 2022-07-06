package com.huangrx.distributedid;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.Test;

/**
 * 雪花算法生成分布式ID
 *
 * @author hrenxiang
 * @since 2022-07-06 10:49
 */
public class SnowflakeDemoTest {

    @Test
    void hutoolGenerateSnowFlake() {
        // 实例化生成 ID 工具对象
        Snowflake snowflake = IdUtil.getSnowflake();
        long nextId = snowflake.nextId();
        System.out.println(snowflake);
        System.out.println(nextId);
    }
}
