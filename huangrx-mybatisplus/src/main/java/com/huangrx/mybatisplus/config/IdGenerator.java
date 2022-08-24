package com.huangrx.mybatisplus.config;

import java.util.Date;
import java.util.UUID;

/**
 * IdGenerator是一个缩了位的雪花ID生成算法，生成的位数是16位，不会导致JS精度丢失
 *
 * Mybatis Plus如果不做任何主键策略配置，默认使用的是雪花算法。
 * 该策略会根据雪花算法生成主键ID，主键类型为Long或String（具体到MySQL数据库就是BIGINT和VARCHAR），
 * 该策略使用接口IdentifierGenerator的方法nextId（默认实现类为DefaultIdentifierGenerator雪花算法）
 *
 * @author        hrenxiang
 * @since         2022-08-24 09:36:34
 */
public class IdGenerator {

    private static IdGenerator instance = new IdGenerator(0);

    public static IdGenerator initDefaultInstance(int machineId) {
        instance = new IdGenerator(machineId);
        return instance;
    }

    public static IdGenerator getInstance() {
        return instance;
    }

    public static long generateId() {
        return instance.nextId();
    }

    // total bits=53(max 2^53-1：9007199254740992-1)

    // private final static long TIME_BIT = 40;
    private final static long MACHINE_BIT = 5;
    private final static long SEQUENCE_BIT = 8;

    /**
     * mask/max value
     */
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long TIMESTMP_LEFT = MACHINE_BIT + SEQUENCE_BIT;

    private long machineId;
    private long sequence = 0L;
    private long lastStmp = -1L;

    private IdGenerator(long machineId) {
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException(
                    "machineId can't be greater than " + MAX_MACHINE_NUM + " or less than 0");
        }
        this.machineId = machineId;
    }

    /**
     * generate new ID
     *
     * @return
     */
    public synchronized long nextId() {
        long currStmp = getTimestamp();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStmp == lastStmp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0L) {
                currStmp = getNextTimestamp();
            }
        } else {
            sequence = 0L;
        }

        lastStmp = currStmp;

        return currStmp << TIMESTMP_LEFT
                | machineId << MACHINE_LEFT
                | sequence;
    }

    private long getNextTimestamp() {
        long mill = getTimestamp();
        while (mill <= lastStmp) {
            mill = getTimestamp();
        }
        return mill;
    }

    private long getTimestamp() {
        // per 10ms // 10ms
        return System.currentTimeMillis() / 10;
    }

    public static Date parseIdTimestamp(long id) {
        return new Date((id >>> TIMESTMP_LEFT) * 10);
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}