### 导读

- Synchronized可以作用在哪里? 分别通过对象锁和类锁进行举例。
- Synchronized本质上是通过什么保证线程安全的? 分三个方面回答：加锁和释放锁的原理，可重入原理，保证可见性原理。
- Synchronized由什么样的缺陷?  Java Lock是怎么弥补这些缺陷的。
- Synchronized和Lock的对比，和选择?
- Synchronized在使用时有何注意事项?
- Synchronized修饰的方法在抛出异常时,会释放锁吗?
- 多个线程等待同一个snchronized锁的时候，JVM如何选择下一个获取锁的线程?
- Synchronized使得同时只有一个线程可以执行，性能比较差，有什么提升的方法?
- 我想更加灵活地控制锁的释放和获取(现在释放锁和获取锁的时机都被规定死了)，怎么办?
- 什么是锁的升级和降级? 什么是JVM里的偏斜锁、轻量级锁、重量级锁?
- 不同的JDK中对Synchronized有何优化?



### synchronized的使用

- 一把锁只能同时被一个线程获取，没有获得锁的线程只能等待；
- 每个实例都对应有自己的一把锁 (this) ,不同实例之间互不影响；
- 锁对象是 *.class 以及 synchronized 修饰的是 static 方法的时候，所有对象公用同一把锁
- synchronized修饰的方法，无论方法正常执行完毕还是抛出异常，都会释放锁



##### 1. 对象锁

包括方法锁(默认锁对象为this,当前实例对象)和同步代码块锁(自己指定锁对象)

**同步代码块形式，手动指定锁定对象，也可以时this，也可以是自定义的锁**

```java
package com.huangrx.concurrent.synchroniz;

/**
 * @author hrenxiang
 * @since 2022-10-20 18:02:21
 */
public class SynchronizedCodeBlock implements Runnable {
  static SynchronizedCodeBlock instence = new SynchronizedCodeBlock();

  @Override
  public void run() {
    // 同步代码块形式——锁为this,两个线程使用的锁是一样的,线程1必须要等到线程0释放了该锁后，才能执行
    synchronized (this) {
      System.out.println("我是线程" + Thread.currentThread().getName());
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println(Thread.currentThread().getName() + "结束");
    }
  }

  public static void main(String[] args) {
    Thread t1 = new Thread(instence, "0");
    Thread t2 = new Thread(instence, "1");
    t1.start();
    t2.start();
  }
}
```

```java
package com.huangrx.concurrent.synchroniz;

/**
 * @author hrenxiang
 * @since 2022-10-20 18:05:39
 */
public class SynchronizedCodeBlock1 implements Runnable {
  static SynchronizedCodeBlock1 instance = new SynchronizedCodeBlock1();
  /**
   * 创建2把锁
   */
  final Object block1 = new Object();
  final Object block2 = new Object();

  @Override
  public void run() {
    // 这个代码块使用的是第一把锁，当他释放后，后面的代码块由于使用的是第二把锁，因此可以马上执行
    synchronized (block1) {
      System.out.println("block1锁,我是线程" + Thread.currentThread().getName());
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("block1锁,"+Thread.currentThread().getName() + "结束");
    }

    synchronized (block2) {
      System.out.println("block2锁,我是线程" + Thread.currentThread().getName());
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("block2锁,"+Thread.currentThread().getName() + "结束");
    }
  }

  public static void main(String[] args) {
    Thread t1 = new Thread(instance);
    Thread t2 = new Thread(instance);
    t1.start();
    t2.start();
  }
}
```

```java
block1锁,我是线程Thread-0
block1锁,Thread-0结束
block2锁,我是线程Thread-0　　// 可以看到当第一个线程在执行完第一段同步代码块之后，第二个同步代码块可以马上得到执行，因为他们使用的锁不是同一把
block1锁,我是线程Thread-1
block2锁,Thread-0结束
block1锁,Thread-1结束
block2锁,我是线程Thread-1
block2锁,Thread-1结束
```

**同步方法形式，synchronized修饰普通方法，锁对象默认为this**

```java
package com.huangrx.concurrent.synchroniz;

/**
 * Synchronized 普通同步方法形式
 *
 * @author hrenxiang
 * @since 2022-10-20 18:12:16
 */
public class SynchronizedMethod implements Runnable {
  static SynchronizedMethod instence = new SynchronizedMethod();

  @Override
  public void run() {
    method();
  }

  public synchronized void method() {
    System.out.println("我是线程" + Thread.currentThread().getName());
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println(Thread.currentThread().getName() + "结束");
  }

  public static void main(String[] args) {
    Thread t1 = new Thread(instence, "0");
    Thread t2 = new Thread(instence, "1");
    t1.start();
    t2.start();
  }
}
```



##### 2. 类锁

指synchronize修饰静态的方法或指定锁对象为Class对象

**synchronize修饰静态方法**

```java
package com.huangrx.concurrent.synchroniz;

/**
 * 类锁
 *
 * @author hrenxiang
 * @since 2022-10-20 18:14:25
 */
public class SynchronizedClass implements Runnable {
  static SynchronizedClass instance1 = new SynchronizedClass();
  static SynchronizedClass instance2 = new SynchronizedClass();

  @Override
  public void run() {
    method();
  }

  /**
   * synchronized用在静态方法上，默认的锁就是 当前类的class对象
   */
  public synchronized static void method() {
    System.out.println("我是线程" + Thread.currentThread().getName());
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println(Thread.currentThread().getName() + "结束");
  }

  public static void main(String[] args) {
    Thread t1 = new Thread(instance1, "0");
    Thread t2 = new Thread(instance2, "1");
    t1.start();
    t2.start();
  }
}
```

**synchronized指定锁对象为Class对象**

```java
package com.huangrx.concurrent.synchroniz;

/**
 * 类锁，同步代码块锁同一个对象
 *
 * @author hrenxiang
 * @since 2022-10-20 18:16:29
 */
public class SynchronizedClass1 implements Runnable {
  static SynchronizedClass1 instence1 = new SynchronizedClass1();
  static SynchronizedClass1 instence2 = new SynchronizedClass1();

  @Override
  public void run() {
    // 所有线程需要的锁都是同一把
    synchronized(SynchronizedClass1.class){
      System.out.println("我是线程" + Thread.currentThread().getName());
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println(Thread.currentThread().getName() + "结束");
    }
  }

  public static void main(String[] args) {
    Thread t1 = new Thread(instence1);
    Thread t2 = new Thread(instence2);
    t1.start();
    t2.start();
  }
}
```



### Synchronized 8锁问题

看下面这段儿代码，回答后面的8个问题：

```java
class Phone {

    public synchronized void sendSMS() throws Exception {
        //TimeUnit.SECONDS.sleep(4);
        System.out.println("------sendSMS");
    }

    public synchronized void sendEmail() throws Exception {
        System.out.println("------sendEmail");
    }

    public void getHello() {
        System.out.println("------getHello");
    }

}

public class Lock_8 {

    public static void main(String[] args) throws Exception {

        Phone phone = new Phone();
        Phone phone2 = new Phone();

        new Thread(() -> {
            try {
                phone.sendSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "AA").start();

        Thread.sleep(100);

        new Thread(() -> {
            try {
                phone.sendEmail();
                //phone.getHello();
                //phone2.sendEmail();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "BB").start();
    }
}
```

多线程的8个问题：
[synchronized 锁的是当前对象本身]

1. 标准访问，先打印短信还是邮件 [sms email]
2. 停4秒在短信方法内，先打印短信还是邮件 [sms email]
3. 停4秒在短信方法内，新增普通的hello方法，是先打短信还是hello [hello 4秒后sms]
4. 停4秒在短信方法内，现在有两部手机，先打印短信还是邮件 [email 四秒后sms]
5. 两个静态同步方法，1部手机，先打印短信还是邮件 [sms email 静态同步方法锁的是当前class对象]
6. 两个静态同步方法，2部手机，先打印短信还是邮件 [sms email 静态同步方法锁的是当前class对象]
7. 1个静态同步方法，1个普通同步方法，1部手机，先打印短信还是邮件 [email sms 锁的class和当前实例对象是不一样的]
8. 1个静态同步方法，1个普通同步方法，2部手机，先打印短信还是邮件 [email sms]



### Synchronized原理分析

##### 1. 加锁和释放锁的原理

> 现象、时机(内置锁this)、深入JVM看字节码(反编译看monitor指令)

深入JVM看字节码，创建如下的代码：
```java
package com.huangrx.concurrent.synchroniz;

/**
 * 原理
 *
 * @author hrenxiang
 * @since 2022-10-21 10:00
 */
public class SynchronizedPrinciple {

  Object object = new Object();
  public void method1() {
    synchronized (object) {

    }
    method2();
  }

  private static void method2() {

  }
}

```


使用javac命令进行编译生成.class文件
```bash
>javac SynchronizedPrinciple.java
```


使用javap命令反编译查看.class文件的信息
```bash
>javap -verbose SynchronizedDemo2.class
```

```shell
public class com.huangrx.concurrent.synchroniz.SynchronizedPrinciple
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #2.#20         // java/lang/Object."<init>":()V
   #2 = Class              #21            // java/lang/Object
   #3 = Fieldref           #5.#22         // com/huangrx/concurrent/synchroniz/SynchronizedPrinciple.object:Ljava/lang/Object;
   #4 = Methodref          #5.#23         // com/huangrx/concurrent/synchroniz/SynchronizedPrinciple.method2:()V
   #5 = Class              #24            // com/huangrx/concurrent/synchroniz/SynchronizedPrinciple
   #6 = Utf8               object
   #7 = Utf8               Ljava/lang/Object;
   #8 = Utf8               <init>
   #9 = Utf8               ()V
  #10 = Utf8               Code
  #11 = Utf8               LineNumberTable
  #12 = Utf8               method1
  #13 = Utf8               StackMapTable
  #14 = Class              #24            // com/huangrx/concurrent/synchroniz/SynchronizedPrinciple
  #15 = Class              #21            // java/lang/Object
  #16 = Class              #25            // java/lang/Throwable
  #17 = Utf8               method2
  #18 = Utf8               SourceFile
  #19 = Utf8               SynchronizedPrinciple.java
  #20 = NameAndType        #8:#9          // "<init>":()V
  #21 = Utf8               java/lang/Object
  #22 = NameAndType        #6:#7          // object:Ljava/lang/Object;
  #23 = NameAndType        #17:#9         // method2:()V
  #24 = Utf8               com/huangrx/concurrent/synchroniz/SynchronizedPrinciple
  #25 = Utf8               java/lang/Throwable
{
  java.lang.Object object;
    descriptor: Ljava/lang/Object;
    flags:

  public com.huangrx.concurrent.synchroniz.SynchronizedPrinciple();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=3, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: aload_0
         5: new           #2                  // class java/lang/Object
         8: dup
         9: invokespecial #1                  // Method java/lang/Object."<init>":()V
        12: putfield      #3                  // Field object:Ljava/lang/Object;
        15: return
      LineNumberTable:
        line 9: 0
        line 11: 4

  public void method1();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=2, locals=3, args_size=1
         0: aload_0
         1: getfield      #3                  // Field object:Ljava/lang/Object;
         4: dup
         5: astore_1
         6: monitorenter
         7: aload_1
         8: monitorexit
         9: goto          17
        12: astore_2
        13: aload_1
        14: monitorexit
        15: aload_2
        16: athrow
        17: invokestatic  #4                  // Method method2:()V
        20: return
      Exception table:
         from    to  target type
             7     9    12   any
            12    15    12   any
      LineNumberTable:
        line 13: 0
        line 15: 7
        line 16: 17
        line 17: 20
      StackMapTable: number_of_entries = 2
        frame_type = 255 /* full_frame */
          offset_delta = 12
          locals = [ class com/huangrx/concurrent/synchroniz/SynchronizedPrinciple, class java/lang/Object ]
          stack = [ class java/lang/Throwable ]
        frame_type = 250 /* chop */
          offset_delta = 4
}
SourceFile: "SynchronizedPrinciple.java"
```



关注`monitorenter`和`monitorexit`即可。

`Monitorenter`和`Monitorexit`指令，会让对象在执行，使其锁计数器加1或者减1。每一个对象在同一时间只与一个monitor(锁)相关联，而一个monitor在同一时间只能被一个线程获得，一个对象在尝试获得与这个对象相关联的Monitor锁的所有权的时候，monitorenter指令会发生如下3中情况之一：

- monitor计数器为0，意味着目前还没有被获得，那这个线程就会立刻获得然后把锁计数器+1，一旦+1，别的线程再想获取，就需要等待
- 如果这个monitor已经拿到了这个锁的所有权，又重入了这把锁，那锁计数器就会累加，变成2，并且随着重入的次数，会一直累加
- 这把锁已经被别的线程获取了，等待锁释放

`monitorexit指令`：释放对于monitor的所有权，释放过程很简单，就是讲monitor的计数器减1，如果减完以后，计数器不是0，则代表刚才是重入进来的，当前线程还继续持有这把锁的所有权，如果计数器变成0，则代表当前线程不再拥有该monitor的所有权，即释放锁。



##### 2. 可重入原理：加锁次数计数器

上面的demo中在执行完同步代码块之后紧接着再会去执行一个静态同步方法，而这个方法锁的对象依然就这个类对象，那么这个正在执行的线程还需要获取该锁吗? 

答案是不必的，从上图中就可以看出来，执行静态同步方法的时候就只有一条monitorexit指令，并没有monitorenter获取锁的指令。这就是锁的重入性，即在同一锁程中，线程不需要再次获取同一把锁。

Synchronized先天具有重入性。每个对象拥有一个计数器，当线程获取该对象锁后，计数器就会加一，释放锁后就会将计数器减一。



##### 3. 保证可见性的原理：内存模型和happens-before规则

Synchronized的happens-before规则，即监视器锁规则：对同一个监视器的解锁，happens-before 于对该监视器的加锁。

==**happens-before规则**：阐述操作之间的内存可见性，一个操作执行的结果需要对另一个操作可见，那么这两个操作之间必须存在 happens-before 关系。==

1、程序顺序规则：一个线程中的每个操作，happens-before 于该线程中的任意后续操作。 也就是前面做的操作，后面是可见的。
2、监视器锁规则：对一个监视器锁的解锁，happens- before 于随后对这个监视器锁的加锁。 即相同的锁，相同的代码块，前面加锁做的操作，对后加锁都是可见的。
3、volatile 变量规则：对一个 volatile 域的写，happens- before 于任意后续对这个 volatile 域的读。 例如A线程对volatile x的值修改为2，B线程get x，能够感知到x的值变为了2。
4、传递性：如果 A happens- before B，且 B happens- before C，那么 A happens- before C。
5、线程 start() 规则：线程A中调用了线程B的start()方法，那么线程A调用线程B的start()方法之前的操作，对线程B都是可见的。 也就是该 start() 操作 Happens-Before 于线程 B 中的任意操作。
6、线程 join() 规则：线程 A 调用子线程 B 的 join() 方法，当线程 B 完成后返回，线程A能够看到线程B中的操作。当然所谓的“看到”，指的是对共享变量的操作。


### JVM中锁的优化

> 以后补充



### Synchronized与Lock

##### 1. synchronized的缺陷

- `效率低`：锁的释放情况少，只有代码执行完毕或者异常结束才会释放锁；试图获取锁的时候不能设定超时，不能中断一个正在使用锁的线程，相对而言，Lock可以中断和设置超时
- `不够灵活`：加锁和释放的时机单一，每个锁仅有一个单一的条件(某个对象)，相对而言，读写锁更加灵活
- `无法知道是否成功获得锁`，相对而言，Lock可以拿到状态，如果成功获取锁，....，如果获取失败，.....



##### 2. Lock解决相应问题

概念

* JDK1.5 以后将同步和锁封装成了对象。并将操作锁的隐式方式定义到了对象中，将隐式动作编程了显示动作。对多线程中的内部细节进行了升级改良。
* 它的出现替代了同步代码块或者同步函数。将同步的隐式锁操作编程了显示锁操作。同时更为灵活。可以一个锁上加上多组监视器。



主要看里面的4个方法:

- `lock()`: 加锁
- `unlock()`: 解锁
- `tryLock()`: 尝试获取锁，返回一个boolean值
- `tryLock(long,TimeUtil)`: 尝试获取锁，可以设置超时

Synchronized只有锁只与一个条件(是否获取锁)相关联，不灵活，后来`Condition与Lock的结合`解决了这个问题。

多线程竞争一个锁时，其余未得到锁的线程只能不停的尝试获得锁，而不能中断。高并发的情况下会导致性能下降。ReentrantLock的lockInterruptibly()方法可以优先考虑响应中断。 一个线程等待时间过长，它可以中断自己，然后ReentrantLock响应这个中断，不再让这个线程继续等待。有了这个机制，使用ReentrantLock时就不会像synchronized那样产生死锁了。

> `ReentrantLock`为常用类，它是一个可重入的互斥锁 Lock，它具有与使用 synchronized 方法和语句所访问的隐式监视器锁相同的一些基本行为和语义，但功能更强大。



##### 3. Lock与synchronized对比

* Lock是显示锁 (手动开启和关闭,别忘记关闭锁) ，synchronized是隐式锁,出了作用域自动释放

* Lock只有代码块锁,synchronized有代码块锁和方法锁

* 使用lock锁,JVM将花费较少的时间来调度线程,性能更好,并且具有更好的扩展性(提供更多的子类)

* 优先使用顺序：Lock > 同步代码块 > 同步方法



##### 5. Condition

* **Lock 接口替代了 synchronized ；Condition 替代了 Object 类中监视器方法 wait，notify，notifyAll。**

* JDK1.5后 将监视器方法单独封装成了 Condition 对象。而且一个锁上可以组合多组监视器对象

  实现了多生产者多消费者时，本方只唤醒对方中一个的操作，提高效率。

* await()睡眠; signal(), signalAll()唤醒;将这些监视器方法单独进行封装，变成了Condition 监视器对象。
  可以任意锁进行组合。使用为一般是生产者是被消费者唤醒，消费者是被生产者唤醒。



### 再深入理解

synchronized是通过软件(JVM)实现的，简单易用，即使在JDK5之后有了Lock，仍然被广泛地使用。

- **使用Synchronized有哪些要注意的？**
    - 锁对象不能为空，因为锁的信息都保存在对象头里
    - 作用域不宜过大，影响程序执行的速度，控制范围过大，编写代码也容易出错
    - 避免死锁
    - 在能选择的情况下，既不要用Lock也不要用synchronized关键字，用java.util.concurrent包中的各种各样的类，如果不用该包下的类，在满足业务的情况下，可以使用synchronized关键，因为代码量少，避免出错
- **synchronized是公平锁吗？**
    - synchronized实际上是**非公平的**，新来的线程有可能立即获得监视器，而在等待区中等候已久的线程可能再次等待，不过这种抢占的方式可以预防饥饿。



### 引用文章

著作权归https://pdai.tech。 链接：https://www.pdai.tech/md/java/thread/java-thread-x-juc-executor-FutureTask.html