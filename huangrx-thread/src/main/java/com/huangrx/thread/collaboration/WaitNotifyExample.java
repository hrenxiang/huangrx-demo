package com.huangrx.thread.collaboration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程不再活动，不再参与调度，进入 wait set 中，因此不会浪费 CPU 资源，也不会去竞争锁了，
 * 这时的线程状态即是 **WAITING**。它还要等着别的线程执行一个 **特别的动作**，
 * 也即是“通知（notify）”在这个对象上等待的线程从wait set 中释放出来，重新进入到调度队列（ready queue）中<br/><br/>
 *
 *
 * <p>注意，消费者被唤醒后是从wait()方法（被阻塞的地方）后面执行，而不是重新从同步块开头。</p>
 * @author hrenxiang
 * @since 2022-10-20 17:37:58
 */
public class WaitNotifyExample {
    public synchronized void before() {
        System.out.println("before");
        notifyAll();
    }

    public synchronized void after() {
        try {
            // wait(); 会释放当前的锁，如果不释放，就无法进入到下一个方法中 调用notifyAll来唤醒等待中的线程
            wait();
            // sleep(); 无法释放锁
            // Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("after");
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        WaitNotifyExample example = new WaitNotifyExample();
        executorService.execute(() -> example.after());
        executorService.execute(() -> example.before());
    }
}