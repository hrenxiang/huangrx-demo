package com.huangrx.concurrent.cas;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author hrenxiang
 * @since 2022-10-21 14:03:50
 */
public class AtomicReferenceTest {

    public static void main(String[] args) {

        // 创建两个Person对象，它们的id分别是101和102。
        Person p1 = new Person(101);
        Person p2 = new Person(102);
        // 新建AtomicReference对象，初始化它的值为p1对象
        AtomicReference ar = new AtomicReference(p1);
        // 通过CAS设置ar。如果ar的值为p1的话，则将其设置为p2。
        ar.compareAndSet(p1, p2);

        Person p3 = (Person) ar.get();
        System.out.println("p3 is " + p3);
        // 默认的equals比较的是地址值
        System.out.println("p3.equals(p1)=" + p3.equals(p1));
        System.out.println("p3.equals(p2)=" + p3.equals(p2));
    }
}

class Person {
    /**
     * 不使用volatile修饰也是可以的
     */
    volatile long id;

    public Person(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id:" + id;
    }
}