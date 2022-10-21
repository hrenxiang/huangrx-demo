package com.huangrx.concurrent.cas;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author hrenxiang
 * @since 2022-10-21 14:10:19
 */
public class TestAtomicIntegerFieldUpdater {

    public static void main(String[] args) {
        TestAtomicIntegerFieldUpdater tIA = new TestAtomicIntegerFieldUpdater();
        tIA.doIt();
    }

    public AtomicIntegerFieldUpdater<DataDemo> updater(String name) {
        // AtomicIntegerFieldUpdater 更新类的字段必须使用public volatile修饰
        return AtomicIntegerFieldUpdater.newUpdater(DataDemo.class, name);

    }

    public void doIt() {
        DataDemo data = new DataDemo();
        System.out.println("publicVar = " + updater("publicVar").getAndAdd(data, 2));
        System.out.println("protectedVar = " + updater("protectedVar").getAndAdd(data, 2));

        // 由于在DataDemo类中属性value2/value3,在TestAtomicIntegerFieldUpdater中不能访问
        //System.out.println("privateVar = "+updater("privateVar").getAndAdd(data,2));

        // 报java.lang.IllegalArgumentException
        //System.out.println("staticVar = "+updater("staticVar").getAndIncrement(data));

        // 下面报异常：must be integer
        // 对于AtomicIntegerFieldUpdater和AtomicLongFieldUpdater只能修改int/long类型的字段，不能修改其包装类型(Integer/Long)。
        // 如果要修改包装类型就需要使用AtomicReferenceFieldUpdater。
        //System.out.println("integerVar = "+updater("integerVar").getAndIncrement(data));
        //System.out.println("longVar = "+updater("longVar").getAndIncrement(data));
    }

}

class DataDemo {
    public volatile int publicVar = 3;
    protected volatile int protectedVar = 4;
    private volatile int privateVar = 5;

    public volatile static int staticVar = 10;
    // public  final int finalVar = 11;

    public volatile Integer integerVar = 19;
    public volatile Long longVar = 18L;

}