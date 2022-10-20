### 1. 导读

- 线程有哪几种状态? 分别说明从一种状态到另一种状态转变有哪些方式?
- 通常线程有哪几种使用方式?
- start 和 run 的区别
- 基础线程机制有哪些?
- 线程的中断方式有哪些?
- 线程的互斥同步方式有哪些? 如何比较和选择?
- 线程之间有哪些协作方式?



### 2. 线程调度方式

#### 2.1 两种模型

分时调度模型：所有线程轮流使用cpu使用权，平均分配每个线程占用cpu的时间片

抢占式调度模型：优先让优先级高的使用cpu，如果线程优先级相同，那么随机选择一个，优先级高的线程获取cpu的时间片相对多一些



#### 2.2 随机性

若 计算机只有一个cpu，那么cpu在某一时刻只能执行一条指令，线程只有抢到cpu时间片，才能执行命令

所以 **多线程执行具有随机性**（抢到使用权是随机的）



#### 2.3 有关线程优先级的方法

方法在 Thread类中

* public final int getPriority() 返回此线程的优先级

* public final void setPriority() 更改线程优先级

* 异常
  IllegalArgumentException - 如果优先级不在 MIN_PRIORITY到 MAX_PRIORITY范围内。
  SecurityException - 如果当前线程不能修改此线程。

* **优先级的取值范围**
  MIN_PRIORITY：最小值是1     MAX-PRIORITY：最大值是10		NORM_PRIORITY：默认线程是5

* 线程的优先级高，只是获取cpu时间片的几率大，并不是每一次都一定会按照级别顺序获取



### 3. 线程状态转换

![image-20220926100431063](https://images.huangrx.cn/uploads/2022/09/26/633108b5e34be.png)




##### 新建(New)

创建后尚未启动。

##### 可运行(Runnable)

可能正在运行，也可能正在等待 CPU 时间片。

包含了操作系统线程状态中的 Running 和 Ready。

##### 阻塞(Blocking)

等待获取一个排它锁，如果其线程释放了锁就会结束此状态。

##### 无限期等待(Waiting)

等待其它线程显式地唤醒，否则不会被分配 CPU 时间片。

| 进入方法                                   | 退出方法                             |
| ------------------------------------------ | ------------------------------------ |
| 没有设置 Timeout 参数的 Object.wait() 方法 | Object.notify() / Object.notifyAll() |
| 没有设置 Timeout 参数的 Thread.join() 方法 | 被调用的线程执行完毕                 |
| LockSupport.park() 方法                    | -                                    |



##### 限期等待(Timed Waiting)

无需等待其它线程显式地唤醒，在一定时间之后会被系统自动唤醒。

调用 Thread.sleep() 方法使线程进入限期等待状态时，常常用“使一个线程睡眠”进行描述。（握紧手去睡，拿着锁）

调用 Object.wait() 方法使线程进入限期等待或者无限期等待时，常常用“挂起一个线程”进行描述。(放开手去睡，丢掉锁)

睡眠和挂起是用来描述行为，而阻塞和等待用来描述状态。

阻塞和等待的区别在于，阻塞是被动的，它是在等待获取一个排它锁。而等待是主动的，通过调用 Thread.sleep() 和 Object.wait() 等方法进入。

| 进入方法                                 | 退出方法                                        |
| ---------------------------------------- | ----------------------------------------------- |
| Thread.sleep() 方法                      | 时间结束                                        |
| 设置了 Timeout 参数的 Object.wait() 方法 | 时间结束 / Object.notify() / Object.notifyAll() |
| 设置了 Timeout 参数的 Thread.join() 方法 | 时间结束 / 被调用的线程执行完毕                 |
| LockSupport.parkNanos() 方法             | -                                               |
| LockSupport.parkUntil() 方法             | -                                               |



##### 死亡(Terminated)

可以是线程结束任务之后自己结束，或者产生了异常而结束。



### 4. 线程使用方式

有四种使用线程的方法:

- 实现 Runnable 接口；
- 实现 Callable 接口；
- 继承 Thread 类。
- 使用线程池

实现 Runnable 和 Callable 接口的类只能当做一个可以在线程中运行的任务，不是真正意义上的线程，因此最后还需要通过 Thread 来调用。可以说任务是通过线程驱动从而执行的。



##### 4.1 实现 Runnable 接口

需要实现 run() 方法。

通过 Thread 调用 start() 方法来启动线程。

```java
public class MyRunnable implements Runnable {
    public void run() {
        // ...
    }
}   
```

```java
public static void main(String[] args) {
    MyRunnable instance = new MyRunnable();
    Thread thread = new Thread(instance);
    thread.start();
}
```

匿名内部类

```java
new Thread(new Runnable() {
    @Override
    public void run() {
 		// 调用资源方法，完成业务逻辑
    }
}, "your thread name").start();
```

lambda表达式

```java
new Thread(() -> {

}, "your thread name").start();
```



##### 4.2 实现 Callable 接口

与 Runnable 相比，Callable 可以有返回值，返回值通过 FutureTask 进行封装。

```java
public class MyCallable implements Callable<Integer> {
    public Integer call() {
        return 123;
    }
}
```

```java
public static void main(String[] args) throws ExecutionException, InterruptedException {
    MyCallable mc = new MyCallable();
    FutureTask<Integer> ft = new FutureTask<>(mc);
    Thread thread = new Thread(ft);
    thread.start();
    System.out.println(ft.get());
}
```



##### 4.3 继承 Thread 类

同样也是需要实现 run() 方法，因为 Thread 类也实现了 Runable 接口。

```java
//分配一个新的线程对象。
public Thread()

//分配一个指定名字的新的线程对象。
public Thread(String name)

//分配一个带有指定目标新的线程对象。
public Thread(Runnable target)

//分配一个带有指定目标新的线程对象并指定名字。
public Thread(Runnable target,String name)
  
//获取当前线程名称。
public String getName()

//导致此线程开始执行; Java虚拟机调用此线程的run方法。
public void start()

//此线程要执行的任务在此处定义代码。
public void run()

//使当前正在执行的线程以指定的毫秒数暂停（暂时停止执行）。
public static void sleep(long millis)

//返回对当前正在执行的线程对象的引用。
public static Thread currentThread()
```

当调用 start() 方法启动一个线程时，虚拟机会将该线程放入就绪队列中等待被调度，当一个线程被调度时会执行该线程的 run() 方法。

```java
public class MyThread extends Thread {
    public void run() {
        // ...
    }
}
```

```java
public static void main(String[] args) {
    MyThread mt = new MyThread();
    mt.start();
}  
```



##### 4.4 实现接口 VS 继承 Thread

实现接口会更好一些，因为:

- Java 不支持多重继承，因此继承了 Thread 类就无法继承其它类，但是可以实现多个接口；
- 类可能只要求可执行就行，继承整个 Thread 类开销过大。



##### 4.5 callable接口与runnable接口的区别？

相同点：**都是接口，都可以编写多线程程序，都采用Thread.start()启动线程**

不同点：

1. 具体方法不同：一个是run，一个是call
2. Runnable没有**返回值**；Callable可以返回执行结果，是个泛型
3. Callable接口的call()方法允许抛出**异常**；Runnable的run()方法异常只能在内部消化，不能往上继续抛
4. 它提供了检查计算是否完成的方法，以等待计算的完成，并检索计算的结果。



##### 4.5 面试题：获得多线程的方法几种

==传统的是继承thread类和实现runnable接口==

==java5以后又有实现callable接口和java的线程池获得==



### 5. start run的区别

调用start方法开启了线程，而run方法只是普通方法的调用，还是在一条线程中顺序执行

**start()方法释义：**

- 用start方法来启动线程，真正实现了多线程运行，这时无需等待 run()方法体代码执行完毕而直接继续执行下面的代码。
- 通过调用 Thread类的 start()方法来启动一个线程，这时此线程处于就绪（可运行）状态，并没有运行，一旦得到cpu时间片，就开始执行run()方法，这里方法 run()称为 线程体，它包含了要执行的这个线程的内容，Run方法运行结束，此线程随即终止。
- start() 方法执行，将自动调用 run()方法，这是由 jvm内存机制规定的



**run() 方法释义：**

* run() 方法只是 Thread类 的一个普通方法，程序中只有主线程这一个线程，其程序路径还是一条，还是要顺序执行，还是要等待
  run() 方法执行完毕后才能执行其他代码

* 把需要并行的代码放到 run() 方法里，调用 start方法即可启动线程，抢到cpu时间片，就会运行run方法，以及run方法内部的代码

* 为什么 重写 run() 方法？因为 run()方法是用来封装线程执行代码的



### 6. start后线程状态

- 调用 start方法，线程进入就绪状态，此时线程对象 仅有执行资格，没有执行权
- 当就绪的线程抢到 执行权时（进入运行状态），方可调用 run方法，run方法执行完毕后，线程死亡，不能复生
- 在没有执行完时，也可以调用stop方法来结束线程，强制死亡
- 若在运行时 调用了 sleep / wait 方法，也会进入阻塞状态，此时释放执行资格和执行权。
- 等 sleep / wait状态结束时，或者调用notifyall方法，该线程被唤醒，又进入就绪状态



### 7. 基础线程机制

#### 7.1 Executor

Executor 管理多个异步任务的执行，而无需程序员显式地管理线程的生命周期。这里的异步是指多个任务的执行互不干扰，不需要进行同步操作。

主要有以下几种 Executor:

##### 7.1.1 Single Thread Executor

```java
单线程线程池：Single Thread Executor : 只有一个线程的线程池，因此所有提交的任务是顺序执行，

代码：Executors.newSingleThreadExecutor()

说明：创建大小为1的固定线程池，同时执行任务(task)的只有一个,其它的（任务）task都放在
LinkedBlockingQueue中排队等待执行。
```

```java
package com.huangrx.thread.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 单线程执行器 <br/>
 * 只有一个线程的线程池,所有提交的任务是顺序执行
 *
 * @author hrenxiang
 * @since 2022-10-20 13:57
 */
public class SingleThreadExecutor {

    public static void main(String[] args) {
        // 报红不影响运行，建议手动创建线程池
        ExecutorService executor = Executors.newSingleThreadExecutor();

        AtomicInteger num = new AtomicInteger(0);

        for (int i = 0; i < 5; i++) {

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " SingleThreadExecutor --> 单线程执行器 --> 1 -->" + num.addAndGet(1));
                }
            });
        }

        for (int i = 0; i < 5; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " SingleThreadExecutor --> 单线程执行器 --> 2 -->" + num.addAndGet(1));
                }
            });
        }
    }
}

```


##### 7.1.2 Cached Thread Pool

```
缓存线程池：Cached Thread Pool : 线程池里有很多线程需要同时执行，老的可用线程将被新的任务触发重新执行，如果线程超过60秒内没执行，那么将被终止并从池中删除，

代码：Executors.newCachedThreadPool()

说明：使用时，放入线程池的task任务会复用线程或启动新线程来执行，注意事项：启动的线程数如果超过整型最大值后会抛出RejectedExecutionException异常，启动后的线程存活时间为一分钟。
```

```java
package com.huangrx.thread.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 缓存线程池 <br/>
 * 1、不按顺序<br/>
 * 2、会复用老的未删除的线程池，也会创建新的线程<br/>
 * 3、线程最大数量是Integer.MAX_VALUE<br/>
 * 4、最大存活时间 60s，如果60s内没用过会删除<br/><br/>
 *
 * @author hrenxiang
 * @since 2022-10-20 14:04
 */
public class CachedThreadPool {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();

        AtomicInteger num = new AtomicInteger(0);

        for (int i = 0; i < 5; i++) {

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " CachedThreadPool --> 缓存线程池 --> 1 -->" + num.addAndGet(1));
                }
            });
        }

        for (int i = 0; i < 5; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " CachedThreadPool --> 缓存线程池 --> 2 -->" + num.addAndGet(1));
                }
            });
        }
    }
}
```


##### 7.1.3 Fixed Thread Pool

```
固定线程数线程池：Fixed Thread Pool : 拥有固定线程数的线程池，如果没有任务执行，那么线程会一直等待，

代码：Executors.newFixedThreadPool()
说明：创建固定大小(nThreads,大小不能超过int的最大值)的线程池，缓冲任务的队列为LinkedBlockingQueue,大小为整型的最大数，当使用此线程池时，在同执行的任务数量超过传入的线程池大小值后，将会放入LinkedBlockingQueue，在LinkedBlockingQueue中的任务需要等待线程空闲后再执行，如果放入LinkedBlockingQueue中的任务超过整型的最大数时，抛出RejectedExecutionException。（newFixedThreadPool的参数指定了可以运行的线程的最大数目，超过这个数目的线程加进去以后，不会运行。其次，加入线程池的线程属于托管状态，线程的运行不受加入顺序的影响。）
```

```java
package com.huangrx.thread.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 固定线程数的线程池<br/>
 * 1、拥有固定线程数 <br/>
 * 2、如果没有任务执行，线程会一直等待 <br/>
 * 3、线程池大小可以自定义，但是最大固定大小是 Integer.MAX_VALUE<br/>
 *
 * @author hrenxiang
 * @since 2022-10-20 14:20
 */
public class FixedThreadPool {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(Integer.MAX_VALUE);

        AtomicInteger num = new AtomicInteger(0);

        for (int i = 0; i < 15; i++) {

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " FixedThreadPool --> 固定线程数的线程池 --> 1 -->" + num.addAndGet(1));
                }
            });
        }

        for (int i = 0; i < 15; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " FixedThreadPool --> 固定线程数的线程池 --> 2 -->" + num.addAndGet(1));
                }
            });
        }
    }
}
```


##### 7.1.4 Scheduled Thread Pool

```
Scheduled Thread Pool : 用来调度即将执行的任务的线程池，

代码：Executors.newScheduledThreadPool()
```

```java
package com.huangrx.thread.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 调度即将执行的任务的线程池
 *
 * @author hrenxiang
 * @since 2022-10-20 14:27
 */
public class ScheduledThreadPool {

    public static void main(String[] args) {

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);

        AtomicInteger num = new AtomicInteger(0);

        for (int i = 0; i < 15; i++) {

            // 调用 ScheduledExecutorService的 schedule方法，可以指定延迟时间
            executor.schedule(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " ScheduledThreadPool --> 调度即将执行的任务的线程池 --> 1 -->" + num.addAndGet(1));
                }
                // 延迟10秒后 执行run方法
            }, 10000, TimeUnit.MILLISECONDS);
        }

        for (int i = 0; i < 15; i++) {
            executor.schedule(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " ScheduledThreadPool --> 调度即将执行的任务的线程池 --> 2 -->" + num.addAndGet(1));
                }
                // 延迟两秒后执行run方法
            }, 2000, TimeUnit.MILLISECONDS);
        }
    }
}
```

##### 7.1.5 Single Thread Scheduled Pool

```
单线程周期性线程池：Single Thread Scheduled Pool : 只有一个线程，用来调度执行将来的任务，代码：
Executors.newSingleThreadScheduledExecutor()
说明：线程keepAliveTime为0，缓存任务的队列为DelayedWorkQueue，注意不要超过整型的最大值。
```



#### 7.2 Daemon

守护线程是程序运行时在后台提供服务的线程，不属于程序中不可或缺的部分。

当所有非守护线程结束时，程序也就终止，同时会杀死所有守护线程。

main() 属于非守护线程。

使用 setDaemon() 方法将一个线程设置为守护线程。**注意：守护线程必须在start之前设置，否则会报错。**

```java
public static void main(String[] args) {
    Thread thread = new Thread(new MyRunnable());
    thread.setDaemon(true);
}
```



#### 7.3 sleep

Thread.sleep(millisec) 方法会休眠当前正在执行的线程，millisec 单位为毫秒。

sleep() 可能会抛出 InterruptedException，因为异常不能跨线程传播回 main() 中，因此必须在本地进行处理。线程中抛出的其它异常也同样需要在本地进行处理。

```java
public void run() {
    try {
        Thread.sleep(3000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
```



#### 7.4 yield

* `static void yield()`：使当前正在执行的线程向另外一个线程**交出运行权**，静态方法。
* 礼让线程，使当前正在执行的线程暂停，但不阻塞，从运行状态变为就绪状态，使cpu重新调度，但是 调度不一定成功，要看 cpu心情。
* ==该方法只是对线程调度器的一个建议，而且也只是建议具有相同优先级的其它线程可以运行。==



### 8. 线程中断方法

一个线程执行完毕之后会自动结束，如果在运行过程中发生异常也会提前结束。

#### 8.1 InterruptedException

通过调用一个线程的 interrupt() 来中断该线程，如果该线程处于**阻塞、限期等待或者无限期**等待状态，那么就会抛出 InterruptedException，从而提前结束该线程。

但是不能中断 I/O 阻塞和 synchronized 锁阻塞。

对于以下代码，在 main() 中启动一个线程之后再中断它，由于线程中调用了 Thread.sleep() 方法，因此会抛出一个 InterruptedException，从而提前结束线程，不执行之后的语句。

```java
package com.huangrx.thread.interrupt;

/**
 * 中断方法
 *
 * @author hrenxiang
 * @since 2022-10-20 15:11
 */
public class InterruptedExceptionExample {

  private static class MyThread1 extends Thread {
    @Override
    public void run() {
      try {
        Thread.sleep(5000);
        System.out.println("Thread run");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {
    Thread thread1 = new MyThread1();
    thread1.start();
    thread1.interrupt();
    System.out.println("Main run");
  }
}

```



#### 8.2 interrupted()

如果一个线程的 run() 方法执行一个无限循环，并且没有执行 sleep() 等会抛出 InterruptedException 的操作，那么调用线程的 interrupt() 方法就无法使线程提前结束。

但是调用 interrupt() 方法会设置线程的中断标记，此时调用 interrupted() 方法会返回 true。因此可以在循环体中使用 interrupted() 方法来判断线程是否处于中断状态，从而提前结束线程。

```java
package com.huangrx.thread.interrupt;

/**
 * run 方法不抛出 InterruptedException 异常，不能使用  thread.interrupt();直接结束
 * 但是，调用thread.interrupt()会给线程打上一个线程中断的标记
 *
 * @author hrenxiang
 * @since 2022-10-20 15:18:37
 */
public class InterruptedExample {

  private static class MyThread2 extends Thread {
    @Override
    public void run() {
      while (!interrupted()) {
        System.out.println("InterruptedExample");
      }
      System.out.println("Thread end");
    }
  }

  public static void main(String[] args) throws InterruptedException {
    Thread thread2 = new MyThread2();
    thread2.start();
    thread2.interrupt();
    System.out.println("Main run");
  }
}
```



#### 8.3 Executor 的中断操作

调用 Executor 的 shutdown() 方法会等待线程都执行完毕之后再关闭，但是如果调用的是 shutdownNow() 方法，则相当于调用每个线程的 interrupt() 方法。

以下使用 Lambda 创建线程，相当于创建了一个匿名内部线程。

```java
public static void main(String[] args) {
    ExecutorService executorService = Executors.newCachedThreadPool();
    executorService.execute(() -> {
        try {
            Thread.sleep(2000);
            System.out.println("Thread run");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    });
    executorService.shutdownNow();
    System.out.println("Main run");
}
```

如果只想中断 Executor 中的一个线程，可以通过使用 submit() 方法来提交一个线程，它会返回一个 Future<?> 对象，通过调用该对象的 cancel(true) 方法就可以中断线程。

```java
Future<?> future = executorService.submit(() -> {
    // ..
});
future.cancel(true);
```



### 9. 线程互斥同步

Java 提供了两种锁机制来控制多个线程对共享资源的互斥访问，第一个是 JVM 实现的 synchronized，而另一个是 JDK 实现的 ReentrantLock。



#### 9.1 synchronized

synchronized实现同步的基础：Java中的每一个对象都可以作为锁。具体表现为以下3种形式：

1. 对于普通同步方法，锁是当前实例对象。
2. 对于静态同步方法，锁是当前类的Class对象。
3. 对于同步代码块，锁是Synchonized括号里配置的对象

当一个线程试图访问同步代码块时，它首先必须得到锁，退出或抛出异常时必须释放锁。



如果**一个实例对象**的**非静态同步方法**获取锁后，该实例对象的其他非静态同步方法必须等待获取锁的方法释放锁后才能获取锁；可是**不同实例对象**的非静态同步方法因为用的**是不同对象的锁**，所以毋须等待其他实例对象的非静态同步方法释放锁，就可以获取自己的锁。

**所有的静态同步方法用的是同一把锁——类对象本身**。不管是不是同一个实例对象，只要是一个类的对象，一旦一个静态同步方法获取锁之后，其他对象的静态同步方法，都必须等待该方法释放锁之后，才能获取锁。

而静态同步方法（Class对象锁）与非静态同步方法（实例对象锁）之间是不会有竞态条件的。



**普通同步方法**

```java
public synchronized void func () {
    // ...
}
```

**静态同步方法**

```java
public synchronized static void fun() {
    // ...
}
```

**代码块**

```java
public void func() {
    synchronized (this) {
        // ...
    }
}
```
它只作用于同一个对象，如果调用两个对象上的同步代码块，就不会进行同步。



#### 9.2 ReentrantLock

ReentrantLock 是 java.util.concurrent(J.U.C)包中的锁。

```java
public class LockExample {

    private Lock lock = new ReentrantLock();

    public void func() {
        lock.lock();
        try {
            for (int i = 0; i < 10; i++) {
                System.out.print(i + " ");
            }
        } finally {
            lock.unlock(); // 确保释放锁，从而避免发生死锁。
        }
    }
}
```

```java
public static void main(String[] args) {
    LockExample lockExample = new LockExample();
    ExecutorService executorService = Executors.newCachedThreadPool();
    executorService.execute(() -> lockExample.func());
    executorService.execute(() -> lockExample.func());
}
```



##### 3. 比较

**锁的实现**

synchronized 是 JVM 实现的，而 ReentrantLock 是 JDK 实现的。



**独占锁**

synchronized是独占锁，加锁和解锁的过程自动进行，易于操作，但不够灵活。ReentrantLock也是独占锁，**加锁和解锁的过程需要手动进行，不易操作，但非常灵活。**



**锁的内容**

synchronzied锁的是对象，锁是保存在对象头里面的，根据对象头数据来标识是否有线程获得锁/争抢锁；ReentrantLock锁的是线程，根据进入的线程和int类型的state标识锁的获得/争抢。



**响应可中断**

当持有锁的线程长期不释放锁的时候，正在等待的线程可以选择放弃等待，改为处理其他事情。

ReentrantLock 可中断，放弃等待；而 synchronized 不行，获取不到就只能等着。



**公平锁**

公平锁是指多个线程在等待同一个锁时，必须按照申请锁的时间顺序来依次获得锁。

synchronized 中的锁是非公平的，ReentrantLock 默认情况下也是非公平的，但是也可以是公平的。



**可重入**

简介： 可重入锁，也叫做递归锁，是指在一个线程中可以多次获取同一把锁，比如：一个线程在执行一个带锁的方法，该方法中又调用了另一个需要相同锁的方法，则该线程可以直接执行调用的方法【即可重入】，而无需重新获得锁；

synchronized可重入，因为**加锁和解锁自动进行，不必担心最后是否释放锁**；ReentrantLock也可重入，但加锁和解锁需要手动进行，且次数需一样，否则其他线程无法获得锁。



### 10. 线程之间协作

当多个线程可以一起工作去解决某个问题时，如果某些部分必须在其它部分之前完成，那么就需要对线程进行协调。



#### 10.1 join()

在线程中调用另一个线程的 join() 方法，会将当前线程挂起，而不是忙等待，直到目标线程结束。

对于以下代码，虽然 b 线程先启动，但是因为在 b 线程中调用了 a 线程的 join() 方法，b 线程会等待 a 线程结束才继续执行，因此最后能够保证 a 线程的输出先于 b 线程的输出。

```java
package com.huangrx.thread.collaboration;

/**
 * join 挂起当前线程，等待join的线程执行完才回执行当前线程
 * 对于以下代码，虽然 b 线程先启动，但是因为在 b 线程中调用了 a 线程的 join() 方法，b 线程会等待 a 线程结束才继续执行，因此最后能够保证 a 线程的输出先于 b 线程的输出。
 *
 * @author hrenxiang
 * @since 2022-10-20 17:32:16
 */
public class JoinExample {

  private static class A extends Thread {
    @Override
    public void run() {
      System.out.println("A");
    }
  }

  private static class B extends Thread {

    private A a;

    B(A a) {
      this.a = a;
    }

    @Override
    public void run() {
      try {
        a.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("B");
    }
  }


  public static void main(String[] args) {
    A a = new A();
    B b = new B(a);
    b.start();
    a.start();
  }

}
```



#### 10.2 wait() notify() notifyAll()

调用 wait() 使得线程等待某个条件满足，线程在等待时会被挂起，当其他线程的运行使得这个条件满足时，其它线程会调用 notify() 或者 notifyAll() 来唤醒挂起的线程。

它们都属于 Object 的一部分，而不属于 Thread。

**只能用在同步方法或者同步代码块中使用，否则会在运行时抛出 IllegalMonitorStateExeception（因为：必须要通过锁对象调用这2个方法。）。**

> wait：线程不再活动，不再参与调度，进入 wait set 中，因此不会浪费 CPU 资源，也不会去竞争锁了，这时的线程状态即是 **WAITING**。它还要等着别的线程执行一个**特别的动作**，也即是“**通知（notify）**”在这个对象上等待的线程从wait set 中释放出来，重新进入到调度队列（ready queue）中
>
> notify：则选取所通知对象的 wait set 中的一个线程释放；例如，餐馆有空位置后，等候就餐最久的顾客最先入座。
>
> notifyAll：则释放所通知对象的 wait set 上的全部线程。

使用 wait() 挂起期间，线程会释放锁。这是因为，如果没有释放锁，那么其它线程就无法进入对象的同步方法或者同步控制块中，那么就无法执行 notify() 或者 notifyAll() 来唤醒挂起的线程，造成死锁。

```java
package com.huangrx.thread.collaboration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程不再活动，不再参与调度，进入 wait set 中，因此不会浪费 CPU 资源，也不会去竞争锁了，
 * 这时的线程状态即是 **WAITING**。它还要等着别的线程执行一个 **特别的动作**，
 * 也即是“通知（notify）”在这个对象上等待的线程从wait set 中释放出来，重新进入到调度队列（ready queue）中
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
      wait();
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
```


**wait() 和 sleep() 的区别**

- wait() 是 Object 的方法，而 sleep() 是 Thread 的静态方法；
- wait() 会释放锁，sleep() 不会。



###### 10.2.1 虚假唤醒

原因：wait()会释放锁, 在java多线程判断时，不能用if，程序出事出在了判断上面。

**注意，消费者被唤醒后是从wait()方法（被阻塞的地方）后面执行，而不是重新从同步块开头。**

中断和虚假唤醒是可能产生的，所以要用loop循环，if只判断一次，while是只要唤醒就要拉回来再判断一次。

(If 换 while)

```java
class ShareDataOne {
    private Integer number = 0;

    /**
     *  增加1
     */
    public synchronized void increment() throws InterruptedException {
        // 1. 判断
        while (number != 0) {
            this.wait();
        }

        // 2. 干活
        number++;
        System.out.println(Thread.currentThread().getName() + ": " + number);

        // 3. 通知
        this.notifyAll();
    }

    /**
     * 减少1
     */
    public synchronized void decrement() throws InterruptedException {
        // 1. 判断
        while (number != 1) {
            this.wait();
        }

        // 2. 干活
        number--;
        System.out.println(Thread.currentThread().getName() + ": " + number);

        // 3. 通知
        this.notifyAll();
    }
}

/**
 * 现在两个线程，
 * 可以操作初始值为零的一个变量，
 * 实现一个线程对该变量加1，一个线程对该变量减1，
 * 交替，来10轮。
 *
 * 笔记：Java里面如何进行工程级别的多线程编写
 * 1 多线程编程模板（套路）-----上
 *    1.1  线程    操作    资源类
 *    1.2  高内聚  低耦合
 * 2 多线程编程模板（套路）-----中
 *    2.1  判断
 *    2.2  干活
 *    2.3  通知
 * 3 多线程编程模板（套路）-----下
 *    防止虚假唤醒（while）
 */
public class NotifyWaitDemo {

    public static void main(String[] args) {
        ShareDataOne shareDataOne = new ShareDataOne();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    shareDataOne.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "AAA").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    shareDataOne.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "BBB").start();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    shareDataOne.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "CCC").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    shareDataOne.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "DDD").start();
    }
}
```



#### 10.3 await() signal() signalAll()

java.util.concurrent 类库中提供了 Condition 类来实现线程之间的协调，可以在 Condition 上调用 await() 方法使线程等待，其它线程调用 signal() 或 signalAll() 方法唤醒等待的线程。相比于 wait() 这种等待方式，await() 可以指定等待的条件，因此更加灵活。

使用 Lock 来获取一个 Condition 对象。

```java
public class AwaitSignalExample {
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void before() {
        lock.lock();
        try {
            System.out.println("before");
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void after() {
        lock.lock();
        try {
            condition.await();
            System.out.println("after");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

```

```html
before
after
```



使用 condition 修改 wait中的 虚假唤醒的 例子

```java
class ShareDataOne {
    private Integer number = 0;

    final Lock lock = new ReentrantLock(); // 初始化lock锁
    final Condition condition = lock.newCondition(); // 初始化condition对象

    /**
     *  增加1
     */
    public void increment() throws InterruptedException {
        lock.lock(); // 加锁
        try {
            // 1. 判断
            while (number != 0) {
                // this.wait();
                condition.await();
            }

            // 2. 干活
            number++;
            System.out.println(Thread.currentThread().getName() + ": " + number);

            // 3. 通知
            // this.notifyAll();
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 减少1
     */
    public void decrement() throws InterruptedException {
        lock.lock();
        try {
            // 1. 判断
            while (number != 1) {
                // this.wait();
                condition.await();
            }

            // 2. 干活
            number--;
            System.out.println(Thread.currentThread().getName() + ": " + number);

            // 3. 通知
            //this.notifyAll();
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
```





### 11. 引用文章

著作权归https://pdai.tech。 链接：https://www.pdai.tech/md/java/thread/java-thread-x-juc-executor-FutureTask.html