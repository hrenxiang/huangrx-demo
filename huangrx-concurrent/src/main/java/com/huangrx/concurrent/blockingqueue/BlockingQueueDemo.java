package com.huangrx.concurrent.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author hrenxiang
 * @create 2021/9/24 6:32 下午
 */
public class BlockingQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);

        //BlockingQueue<String> queue = new LinkedBlockingDeque<>(3);

        /**
         * BlockingQueue 不接受 null 值的插入，相应的方法在碰到 null 的插入时会抛出 NullPointerException 异常。
         * null 值在这里通常用于作为特殊值返回，代表 poll 失败。
         * 所以，如果允许插入 null 值的话，那获取的时候，就不能很好地用 null 来判断到底是代表失败，还是获取的值就是 null 值。
         */
        //System.out.println(queue.add(null));

        // 检索但不删除此队列的头。此方法与peek的不同之处在于，它在队列为空时抛出异常
        //System.out.println(queue.element());

        // 无论 element在哪调用，都是获取到最前面的
//        System.out.println(queue.add("b"));
//        System.out.println(queue.element());
//        System.out.println(queue.add("c"));
//        System.out.println(queue.element());
//        System.out.println(queue.add("d"));
//
//        System.out.println(queue.remove("a"));
//        System.out.println(queue.remove("b"));
//        System.out.println(queue.remove("c"));
//        System.out.println(queue.remove("d"));
//        System.out.println(queue.remove("e"));
        // 队列为空时，异常
//        System.out.println(queue.element());



        // -----------------------------------------


//        // peek、检索但不删除此队列的头，如果此队列为空则返回null
//        System.out.print(queue.peek() + "\n");
//        System.out.print("a: " + queue.offer("a") + "、");
//        System.out.print(queue.peek() + "\n");
//        System.out.print("b: " + queue.offer("b") + "、");
//        System.out.print(queue.peek() + "\n");
//        System.out.print("c: " + queue.offer("c") + "、");
//        System.out.print(queue.peek() + "\n");
//        System.out.print("d: " + queue.offer("d") + "、");
//        System.out.print(queue.peek() + "\n");
//        System.out.print("null: " + queue.offer("null"));
//        System.out.print(queue.peek() + "\n");
//        // 可以添加空字符串
//        //System.out.println(queue.offer(null));      // 不可以添加空元素
//
//
//        System.out.print(queue.poll() + "、");
//        System.out.print(queue.poll() + "、");
//        System.out.print(queue.poll() + "、");
//        System.out.print(queue.poll() + "\n");               // 超出队列长度，不存在，获取失败，返回 null


        // -----------------------------------------

//        queue.put("a");
//        queue.put("b");
//        queue.put("c");
//        queue.put("d");

//        queue.put("d");
//
//        System.out.println(queue.take());
//        System.out.println(queue.take());
//        System.out.println(queue.take());
//        System.out.println(queue.take());

        // -----------------------------------------
//        System.out.println(queue.offer("null", 3, TimeUnit.SECONDS));
//        System.out.println(queue.offer("b", 3, TimeUnit.SECONDS));
//        System.out.println(queue.offer("c", 3, TimeUnit.SECONDS));
//        System.out.println(queue.offer("d", 3, TimeUnit.SECONDS));
//
//        System.out.println(queue.poll(3, TimeUnit.SECONDS));
//        System.out.println(queue.poll(3, TimeUnit.SECONDS));
//        System.out.println(queue.poll(3, TimeUnit.SECONDS));
//        System.out.println(queue.poll(3, TimeUnit.SECONDS));

    }

}