### 什么是BlockingQueue?
<hr/>

java.util.concurrent包中的 BlockingQueue接口表示一个线程安全的队列，可以放入并获取实例。

通常用于使线程产生对象，而另一线程则使用该对象。这是一张阐明这一原理的图表。

![image-20221028100053379](https://images.huangrx.cn/uploads/2022/10/28/635b37d5f3a43.png)

首先，最基本的来说， BlockingQueue 是一个**先进先出**的队列（Queue），为什么说是阻塞（Blocking）的呢？

- 获取队列中的元素，但是队列为空时，获取状态会被阻塞，等待队列中有元素后再被获取；

- 添加元素到队列中，但是队列已满时，添加状态会被阻塞，等到队列可以放入新元素时再放入；




### BlockingQueue类关系及方法
<hr/>

#### 类关系
BlockingQueue 是一个接口，继承自 Queue，所以其实现类也可以作为 Queue 的实现来使用，而 Queue 又继承自 Collection 接口。
![image-20221028100921469](https://images.huangrx.cn/uploads/2022/10/28/635b39d212bd9.png)

当然，BlockingQueue并不只是有以上图中的几个实现类，还有很多：
![image-20221028102236542](https://images.huangrx.cn/uploads/2022/10/28/635b3ceea8bd8.png)

BlockingQueue接口主要讲以下实现类：

1、<font color="red">ArrayBlockingQueue：底层是数组，有界队列，如果我们要使用生产者-消费者模式，这是非常好的选择。</font>

2、<font color="red">LinkedBlockingQueue：底层是链表（但大小默认值为integer.MAX_VALUE），可以当做无界和有界队列来使用，所以大家不要以为它就是无界队列。</font>

3、<font color="red">SynchronousQueue：本身不带有空间来存储任何元素，也就是单个元素队列，使用上可以选择公平模式和非公平模式。</font>

4、PriorityBlockingQueue：是无界队列，基于数组，数据结构为二叉堆，数组第一个也是树的根节点总是最小值。




#### 方法详解
BlockingQueue 对插入操作、移除操作、获取元素操作提供了四种不同的方法用于不同的场景中使用：
1、抛出异常；
2、返回特殊值（null 或 true/false，取决于具体的操作）；
3、阻塞等待此操作，直到这个操作成功；
4、阻塞等待此操作，直到成功或者超时指定时间。

总结如下：
|             | Throws exception | Special value | Blocks         | Times out            |
| ----------- | ---------------- | ------------- | -------------- | -------------------- |
| **Insert**  | add(e)           | offer(e)      | **put(e)**     | offer(e, time, unit) |
| **Remove**  | remove()         | poll()        | **take()**     | poll(time, unit)     |
| **Examine** | element()        | peek()        | not applicable | not applicable       |

1、抛出异常；
```text
add和remove正常执行返回true，element（不删除）返回阻塞队列中的第一个元素
当阻塞队列满时，再往队列里add插入元素会抛IllegalStateException:Queue full
当阻塞队列空时，再往队列里remove移除元素会 返回false
当阻塞队列空时，再调用element检查元素会抛出NoSuchElementException

队列中不能添加 null类型元素，但是可以添加null类型的字符串
```
![image-20221028093222633](https://images.huangrx.cn/uploads/2022/10/28/635b312717d9b.png)
![image-20221028093342967](https://images.huangrx.cn/uploads/2022/10/28/635b31776f93e.png)

2、返回特殊值（null 或 true/false，取决于具体的操作）；
```text
插入方法 offer，成功ture失败false
移除方法 poll，成功返回出队列的元素，队列里没有就返回null
检查方法 peek，成功返回队列中的第一个（队列最前面的）元素，没有返回null

队列中不能添加 null类型元素，但是可以添加null类型的字符串
```

3、阻塞等待此操作，直到这个操作成功；
```text
put 无返回值
take 返回被删除的元素

如果试图的操作无法立即执行，该方法调用将会发生阻塞，直到能够执行。
当阻塞队列满时，再往队列里put元素，队列会一直阻塞生产者线程直到队列不满可以put数据 or 响应中断退出
当阻塞队列空时，再从队列里take元素，队列会一直阻塞消费者线程直到队列可用

队列中不能添加 null类型元素，但是可以添加null类型的字符串
```

4、阻塞等待此操作，直到成功或者超时指定时间。
```text
如果试图的操作无法立即执行，该方法调用将会发生阻塞，直到能够执行，但等待时间不会超过给定值。
offer 返回一个特定值以告知该操作是否成功(典型的是 true / false)，超过时间返回 false。
		
poll 成功返回被移除的元素，失败或者超过给定时间 返回null

队列中不能添加 null类型元素，但是可以添加null类型的字符串
```




#### 注意事项 (根据java doc编制)
BlockingQueue 不接受 null 值（是 null 不是 "null" ）的插入，相应的方法在碰到 null 的插入时会抛出 NullPointerException 异常。null 值在这里通常用于作为特殊值返回（表格中的第三列），代表 poll 失败。所以，如果允许插入 null 值的话，那获取的时候，就不能很好地用 null 来判断到底是代表失败，还是获取的值就是 null 值。

我们可以用 remove(x) 来删除任意一个元素，但是，这类操作获取除了开始和结尾的位置其实并不高效，所以尽量只在少数的场合使用，比如一条消息已经入队，但是需要做取消操作的时候。

![image-20221028092858049](https://images.huangrx.cn/uploads/2022/10/28/635b305c06827.png)


> 对于 BlockingQueue，我们的关注点应该在 put(e) 和 take() 这两个方法，因为这两个方法是带阻塞的。


一个 BlockingQueue 可能是有界的，如果在插入的时候，发现队列满了，那么 put 操作将会阻塞。通常，在这里我们说的无界队列也不是说真正的无界，而是它的容量是 Integer.MAX_VALUE（21亿多）。

BlockingQueue 是设计用来实现生产者-消费者队列的，当然，你也可以将它当做普通的 Collection 来用，前面说了，它实现了 java.util.Collection 接口。例如，我们可以用 remove(x) 来删除任意一个元素，但是，这类操作通常并不高效，所以尽量只在少数的场合使用，比如一条消息已经入队，但是需要做取消操作的时候。

BlockingQueue 的实现都是线程安全的，但是批量的集合操作如 addAll, containsAll, retainAll 和 removeAll  不一定是原子操作。如 addAll(c) 有可能在添加了一些元素后中途抛出异常，此时 BlockingQueue 中已经添加了部分元素，这个是允许的，取决于具体的实现。

BlockingQueue 不支持 close 或 shutdown 等关闭操作，因为开发者可能希望不会有新的元素添加进去，此特性取决于具体的实现，不做强制约束。

最后，BlockingQueue 在生产者-消费者的场景中，是支持多消费者和多生产者的，说的其实就是线程安全问题。




#### 方法实操
```java
package blockingqueue;

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
```




### BlockingQueue 实现之 ArrayBlockingQueue
<hr/>

ArrayBlockingQueue 是 BlockingQueue 接口的有界队列实现类，底层采用数组来实现。

其并发控制采用可重入锁来控制，不管是插入操作还是读取操作，都需要获取到锁才能进行操作。

它采用一个 ReentrantLock 和相应的两个 Condition 来实现。



#### 属性
```java
// 用于存放元素的数组
final Object[] items;
// 下一次读取操作的位置
int takeIndex;
// 下一次写入操作的位置
int putIndex;
// 队列中的元素数量
int count;

// 以下几个就是控制并发用的同步器
final ReentrantLock lock;
private final Condition notEmpty;
private final Condition notFull;
```

ArrayBlockingQueue 实现并发同步的原理就是，读操作和写操作都需要获取到 AQS 独占锁才能进行操作。
如果队列为空，这个时候读操作的线程进入到读线程队列排队，等待写线程写入新的元素，然后唤醒读线程队列的第一个等待线程。
如果队列已满，这个时候写操作的线程进入到写线程队列排队，等待读线程将队列元素移除腾出空间，然后唤醒写线程队列的第一个等待线程。

对于 ArrayBlockingQueue，我们可以在构造的时候指定以下三个参数：
- 队列容量，其限制了队列中最多允许的元素个数；
- 指定独占锁是公平锁还是非公平锁。非公平锁的吞吐量比较高，公平锁可以保证每次都是等待最久的线程获取到锁；
- 可以指定用一个集合来初始化，将此集合中的元素在构造方法期间就先添加到队列中。



#### PUT方法
```java
public void put(E e) throws InterruptedException {
        checkNotNull(e);
        // 获取队列中唯一的锁对象
        final ReentrantLock lock = this.lock;
        // 线程加锁
        lock.lockInterruptibly();
        try {
            // 如果 全局变量 count（队列中的数量） 和 items （排队项目的数量）相等，说明队列已满，需要 挂起等待，消费 take后，才能再次进行添加；自旋一直判断；
            while (count == items.length)
                // condition: 初始化当前线程，设置waitStatus = -2，放入条件队列，完全释放锁，自循环判断，若没有进入阻塞队列，直接挂起等待唤醒。
                notFull.await();
            // 如果数量没有达到队列最大值，进行入队操作
            enqueue(e);
        } finally {
            // 确保解锁
            lock.unlock();
        }
}
```

```java
private void enqueue(E x) {
        // assert lock.getHoldCount() == 1;
        // assert items[putIndex] == null;
        // 拿到队列中的项目
        final Object[] items = this.items;
        // 获取全局变量索引处的项目，putIndex初始化是0
        items[putIndex] = x;
        // 将putIndex加1，并判断项目数组的长度是否和索引相等，如果相等，将添加项目的全局变量索引置为0
        if (++putIndex == items.length)
            putIndex = 0;
        // items中项目总数量，达到最大值后，外层判断进入条件队列进行等待
        count++;
        // 唤醒消费者 使条件队列中的消费者线程进入阻塞队列中运行
        notEmpty.signal();
}
```


#### TAKE方法
```java
public E take() throws InterruptedException {
        // 获取队列中唯一的锁对象
        final ReentrantLock lock = this.lock;
        // 线程加锁
        lock.lockInterruptibly();
        try {
            // 如果 全局变量 count（队列中的数量）等于 0，说明此时队列中的项目为 0，需要 挂起等待，生产 put后，才能再次获取；自旋一直判断；
            while (count == 0)
                // condition: 初始化当前线程，设置waitStatus = -2，放入条件队列，完全释放锁，自循环判断，若没有进入阻塞队列，直接挂起等待唤醒。
                notEmpty.await();
            return dequeue();
        } finally {
            lock.unlock();
        }
}
```

```java
private E dequeue() {
        // assert lock.getHoldCount() == 1;
        // assert items[takeIndex] != null;
        // 拿到队列中的项目
        final Object[] items = this.items;
        @SuppressWarnings("unchecked")
        // 获取全局变量索引处的项目，takeIndex初始化是0
        E x = (E) items[takeIndex];
        // 获取索引处的项目后，将项目中索引处置为null，items长度不变
        items[takeIndex] = null;
        // 将takeIndex加1，并判断项目数组的长度是否和索引相等，如果相等，将获取的全局变量索引置为0
        if (++takeIndex == items.length)
            takeIndex = 0;
        // 说明items数组中项目减少一个（null处可以放新值了）
        count--;
        // 直接遍历才回用到
        if (itrs != null)
            itrs.elementDequeued();
        // 唤醒生产者 使条件队列中的生产者线程进入阻塞队列中运行
        notFull.signal();
        // 返回拿到的值
        return x;
}
```



### BlockingQueue 实现之 LinkedBlockingQueue
<hr/>

底层基于单向链表实现的阻塞队列，可以当做无界队列也可以当做有界队列来使用。看构造方法：
```java
// 传说中的无界队列
public LinkedBlockingQueue() {
    this(Integer.MAX_VALUE);
}
```

```java
// 传说中的有界队列
public LinkedBlockingQueue(int capacity) {
    if (capacity <= 0) throw new IllegalArgumentException();
    this.capacity = capacity;
    last = head = new Node<E>(null);
}
```

我们看看这个类有哪些属性：
```java
// 队列容量
private final int capacity;

// 队列中的元素数量
private final AtomicInteger count = new AtomicInteger(0);

// 队头
private transient Node<E> head;

// 队尾
private transient Node<E> last;

// take, poll, peek 等读操作的方法需要获取到这个锁
private final ReentrantLock takeLock = new ReentrantLock();

// 如果读操作的时候队列是空的，那么等待 notEmpty 条件
private final Condition notEmpty = takeLock.newCondition();

// put, offer 等写操作的方法需要获取到这个锁
private final ReentrantLock putLock = new ReentrantLock();

// 如果写操作的时候队列是满的，那么等待 notFull 条件
private final Condition notFull = putLock.newCondition();
```

这里用了两个锁，两个 Condition，简单介绍如下：

takeLock 和 notEmpty 怎么搭配：如果要获取（take）一个元素，需要获取 takeLock 锁，但是获取了锁还不够，如果队列此时为空，还需要队列不为空（notEmpty）这个条件（Condition）。

putLock 需要和 notFull 搭配：如果要插入（put）一个元素，需要获取 putLock 锁，但是获取了锁还不够，如果队列此时已满，还需要队列不是满的（notFull）这个条件（Condition）。

首先，这里用一个示意图来看看 LinkedBlockingQueue 的并发读写控制，然后再开始分析源码：
![image-20221028175005447](https://images.huangrx.cn/uploads/2022/10/28/635ba5d00a271.png)

看懂这个示意图，源码也就简单了，读操作是排好队的，写操作也是排好队的，唯一的并发问题在于一个写操作和一个读操作同时进行，只要控制好这个就可以了。

先上构造方法：

```java
public LinkedBlockingQueue(int capacity) {
    if (capacity <= 0) throw new IllegalArgumentException();
    this.capacity = capacity;
    last = head = new Node<E>(null);
}
```

注意，这里会初始化一个空的头结点，那么第一个元素入队的时候，队列中就会有两个元素。读取元素时，也总是获取头节点后面的一个节点。count 的计数值不包括这个头节点。


我们来看下 put 方法是怎么将元素插入到队尾的：

```java
public void put(E e) throws InterruptedException {
    if (e == null) throw new NullPointerException();
    // 如果你纠结这里为什么是 -1，可以看看 offer 方法。这就是个标识成功、失败的标志而已。
    int c = -1;
    Node<E> node = new Node(e);
    final ReentrantLock putLock = this.putLock;
    final AtomicInteger count = this.count;
    // 必须要获取到 putLock 才可以进行插入操作
    putLock.lockInterruptibly();
    try {
        // 如果队列满，等待 notFull 的条件满足。
        while (count.get() == capacity) {
            notFull.await();
        }
        // 入队
        enqueue(node);
        // count 原子加 1，c 还是加 1 前的值
        c = count.getAndIncrement();
        // 如果这个元素入队后，还有至少一个槽可以使用，调用 notFull.signal() 唤醒等待线程。
        // 哪些线程会等待在 notFull 这个 Condition 上呢？
        if (c + 1 < capacity)
            notFull.signal();
    } finally {
        // 入队后，释放掉 putLock
        putLock.unlock();
    }
    // 如果 c == 0，那么代表队列在这个元素入队前是空的（不包括head空节点），
    // 那么所有的读线程都在等待 notEmpty 这个条件，等待唤醒，这里做一次唤醒操作
    if (c == 0)
        signalNotEmpty();
}

// 入队的代码非常简单，就是将 last 属性指向这个新元素，并且让原队尾的 next 指向这个元素
// 这里入队没有并发问题，因为只有获取到 putLock 独占锁以后，才可以进行此操作
private void enqueue(Node<E> node) {
    // assert putLock.isHeldByCurrentThread();
    // assert last.next == null;
    last = last.next = node;
}

// 元素入队后，如果需要，调用这个方法唤醒读线程来读
private void signalNotEmpty() {
    final ReentrantLock takeLock = this.takeLock;
    takeLock.lock();
    try {
        notEmpty.signal();
    } finally {
        takeLock.unlock();
    }
}
```

我们再看看 take 方法：

```java
public E take() throws InterruptedException {
    E x;
    int c = -1;
    final AtomicInteger count = this.count;
    final ReentrantLock takeLock = this.takeLock;
    // 首先，需要获取到 takeLock 才能进行出队操作
    takeLock.lockInterruptibly();
    try {
        // 如果队列为空，等待 notEmpty 这个条件满足再继续执行
        while (count.get() == 0) {
            notEmpty.await();
        }
        // 出队
        x = dequeue();
        // count 进行原子减 1
        c = count.getAndDecrement();
        // 如果这次出队后，队列中至少还有一个元素，那么调用 notEmpty.signal() 唤醒其他的读线程
        if (c > 1)
            notEmpty.signal();
    } finally {
        // 出队后释放掉 takeLock
        takeLock.unlock();
    }
    // 如果 c == capacity，那么说明在这个 take 方法发生的时候，队列是满的
    // 既然出队了一个，那么意味着队列不满了，唤醒写线程去写
    if (c == capacity)
        signalNotFull();
    return x;
}
// 取队头，出队
private E dequeue() {
    // assert takeLock.isHeldByCurrentThread();
    // assert head.item == null;
    // 之前说了，头结点是空的
    Node<E> h = head;
    Node<E> first = h.next;
    h.next = h; // help GC
    // 设置这个为新的头结点
    head = first;
    E x = first.item;
    first.item = null;
    return x;
}
// 元素出队后，如果需要，调用这个方法唤醒写线程来写
private void signalNotFull() {
    final ReentrantLock putLock = this.putLock;
    putLock.lock();
    try {
        notFull.signal();
    } finally {
        putLock.unlock();
    }
}
```


### BlockingQueue 实现之 SynchronousQueue
<hr/>

它是一个特殊的队列，它的名字其实就蕴含了它的特征 - - 同步的队列。为什么说是同步的呢？这里说的并不是多线程的并发问题，而是因为当一个线程往队列中写入一个元素时，写入操作不会立即返回，需要等待另一个线程来将这个元素拿走；同理，当一个读线程做读操作的时候，同样需要一个相匹配的写线程的写操作。这里的 Synchronous 指的就是读线程和写线程需要同步，一个读线程匹配一个写线程。

我们比较少使用到 SynchronousQueue 这个类，不过它在线程池的实现类 ThreadPoolExecutor 中得到了应用，感兴趣的读者可以在看完这个后去看看相应的使用。

虽然上面我说了队列，但是 SynchronousQueue 的队列其实是虚的，其不提供任何空间（一个都没有）来存储元素。数据必须从某个写线程交给某个读线程，而不是写到某个队列中等待被消费。

你不能在 SynchronousQueue 中使用 peek 方法（在这里这个方法直接返回 null），peek 方法的语义是只读取不移除，显然，这个方法的语义是不符合 SynchronousQueue 的特征的。SynchronousQueue 也不能被迭代，因为根本就没有元素可以拿来迭代的。虽然 SynchronousQueue 间接地实现了 Collection 接口，但是如果你将其当做 Collection 来用的话，那么集合是空的。当然，这个类也是不允许传递 null 值的（并发包中的容器类好像都不支持插入 null 值，因为 null 值往往用作其他用途，比如用于方法的返回值代表操作失败）。

接下来，我们来看看具体的源码实现吧，它的源码不是很简单的那种，我们需要先搞清楚它的设计思想。

源码加注释大概有 1200 行，我们先看大框架：

```java
// 构造时，我们可以指定公平模式还是非公平模式，区别之后再说
public SynchronousQueue(boolean fair) {
    transferer = fair ? new TransferQueue() : new TransferStack();
}
abstract static class Transferer {
    // 从方法名上大概就知道，这个方法用于转移元素，从生产者手上转到消费者手上
    // 也可以被动地，消费者调用这个方法来从生产者手上取元素
    // 第一个参数 e 如果不是 null，代表场景为：将元素从生产者转移给消费者
    // 如果是 null，代表消费者等待生产者提供元素，然后返回值就是相应的生产者提供的元素
    // 第二个参数代表是否设置超时，如果设置超时，超时时间是第三个参数的值
    // 返回值如果是 null，代表超时，或者中断。具体是哪个，可以通过检测中断状态得到。
    abstract Object transfer(Object e, boolean timed, long nanos);
}
```

