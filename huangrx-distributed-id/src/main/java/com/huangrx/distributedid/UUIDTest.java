package com.huangrx.distributedid;

import java.util.UUID;

/**
 * @author    hrenxiang
 * @since     2022/7/6 17:03
 */
public class UUIDTest {

    public static void main(String[] args) {
        // 生成 UUID
        String uuid = UUID.randomUUID().toString();
        // 输出 UUID 串
        System.out.println(uuid);
    }

}