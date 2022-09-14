### 本地锁的局限性
之前，我们学习过synchronized及lock锁，这些锁都是本地锁。接下来写一个案例，演示本地锁的问题

#### 编写测试代码
```java
    @Slf4j
@RestController
public class DistributedLockController {

    @Resource
    private DistributedLockService distributedLockService;
    
    @RequestMapping(value = "index/testLock", method = RequestMethod.GET)
    public BaseResponse<Object> testLock(){
        log.info("开始！");
        distributedLockService.testLock();

        return BaseResponse.success("你好啊，这可不是分布式锁！");
    }

}
```
```java
public interface DistributedLockService {
    /**
     * 测试
     */
    void testLock();
}
```
```java
@Service
public class DistributedLockServiceImpl implements DistributedLockService {

    @Resource
    private RedisService redisService;

    @Override
    public void testLock() {
        // 查询redis中的num值，测试前记得先添加这个进入缓存，不然 一直是空，一直return
        Object value = this.redisService.get(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "testLock", "1"));
        // 没有该值return
        if (Objects.isNull(value)){
            return ;
        }
        // 有值就转成成int
        Integer num = (Integer) value;
        // 把redis中的num值+1
        this.redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "testLock", "1"), ++num);
    }
}
```


#### 使用压测工具进行压测
这里使用的是ab测试工具：httpd-tools（yum install -y httpd-tools）
```shell
ab  -n（一次发送的请求数）  -c（请求的并发数） 访问路径
```

进行压测：请求数500，并发量 1（同一时间内一个请求）
```shell
ab -n 500 -c 1 http://localhost:8081/index/testLock
```
进行压测：请求数500，并发量10（同一时间内十个请求）
```shell
ab -n 500 -c 10 http://localhost:8081/index/testLock
```
![image-20220908111511350](https://images.huangrx.cn/uploads/2022/09/08/63195e3fedc1d.png)
![image-20220908111313030](https://images.huangrx.cn/uploads/2022/09/08/63195dcf7f038.png)

由上图中的数据可以看出，在并发请求的情况下，累加到500，但结果竟然只有95，这其中问题很大。


#### 添加本地锁
```java
    @Override
    public synchronized void testSynchronizedLock() {
        // 查询redis中的num值 测试前记得先添加这个进入缓存，不然 一直是空，一直return
        Object value = this.redisService.get(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "testSynchronizedLock", "1"));
        // 没有该值return
        if (Objects.isNull(value)){
        return ;
        }
        // 有值就转成成int
        Integer num = (Integer) value;
        // 把redis中的num值+1
        this.redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "testSynchronizedLock", "1"), ++num);
    }
```

进行压测：请求数500，并发量10（同一时间内十个请求）
![image-20220908112823411](https://images.huangrx.cn/uploads/2022/09/08/631961586670a.png)

上图可以看到，这个样子好像解决了并发请求的问题，但是，真的完美了吗？我们看下集群情况下，会是怎样？


#### 集群模式下并发请求
我们先进行配置，复制 修改端口，代表另外一台服务器，这样我们就有了启动了两个服务（代替集群）
![image-20220908153947126](https://images.huangrx.cn/uploads/2022/09/08/63199c46ab934.png)
![image-20220908153811284](https://images.huangrx.cn/uploads/2022/09/08/63199be39f1e8.png)

集群有了，我们还需要让请求分配到两个服务器上，方便起见，我们再配置一下nginx
![image-20220908154304550](https://images.huangrx.cn/uploads/2022/09/08/63199d08ebf02.png)
遮盖掉的是本地的 IP地址，配置时请自行配置

进行压测：请求数500，并发量10（同一时间内十个请求）请求加了本地锁的方法
![image-20220908154509523](https://images.huangrx.cn/uploads/2022/09/08/63199d85c35db.png)
由上图我们发现，本地锁没用了，结果才327。


#### 本地锁结论
如果是单机情况下（单JVM），线程之间共享内存，只要使用线程锁就可以解决并发问题。
但如果是分布式情况下（多JVM），线程A和线程B很可能不是在同一JVM中，这样线程锁就无法起到作用了，这时候就要用到分布式锁来解决。


### 分布式锁概念和实现方案

#### 概念
随着业务发展的需要，单体单机系统演化成分布式集群系统后，由于分布式系统多线程、多进程并且分布在不同机器上，使得单机部署下的并发控制锁策略失效。
单纯的Java API并不能提供分布式锁的能力。为了解决这个问题就需要一种跨JVM的互斥机制来控制共享资源的访问，这就是分布式锁要解决的问题！


#### 分布式锁主流实现方案
1、基于数据库实现分布式锁
2、基于缓存（Redis等）
3、基于Zookeeper

每一种分布式锁解决方案都有各自的优缺点：
1、性能：redis最高
2、可靠性：zookeeper最高


### 分布式锁（Redis 实现方案）

#### 基本实现
借助于redis中的命令setnx(key, value)，key不存在就新增，存在就什么都不做。
同时有多个客户端发送setnx命令，只有一个客户端可以成功，返回1（true）；其他的客户端返回0（false）。
![image-20220908162546454](https://images.huangrx.cn/uploads/2022/09/08/6319a70b134a7.png)

1、多个客户端同时尝试获取锁（setnx）
2、获取成功，执行业务逻辑，执行完成释放锁（del）
3、其他客户端等待重试

```java
    @RequestMapping(value = "index/testSetNxLock", method = RequestMethod.GET)
    public BaseResponse<Object> testSetNxLock(){
        log.info("开始！");
        distributedLockService.testSetNxLock();

        return BaseResponse.success("你好啊，这离分布式锁近了！");
    } 
```
```java
    /**
      * 分布式锁基础版
      * 
      * 后续修改了一下，给redis缓存的key（锁）添加过期时间，防止死锁（无法释放锁）
      */
    void testSetNxLock();
```
```java
    @Override
    public void testSetNxLock() {
        // 1. 从redis中获取锁,setnx
        Boolean lock = redisService.setNx(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "lock"), "locked");
        if (lock) {
            // 查询redis中的num值
            Integer value = (Integer) redisService.get(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "num"));
            // 没有该值return
            if (Objects.isNull(value)) {
                return;
            }
            // 把redis中的num值+1
            redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "num"), ++value);

            // 2. 释放锁 del
            redisService.del(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "lock"));
        } else {
            // 3. 每隔1秒钟回调一次，再次尝试获取锁
            try {
                Thread.sleep(1000);
                testSetNxLock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
```

进行压测：请求数500，并发量10（同一时间内十个请求）
![image-20220908164139474](https://images.huangrx.cn/uploads/2022/09/08/6319aac41b83c.png)

由压测可知问题得到了基本的解决，但还有问题！

问题：setnx刚好获取到锁，业务逻辑出现异常，导致锁无法释放
解决：设置过期时间，自动释放锁。


#### 优化 - 设置锁的过期时间
问题：setnx刚好获取到锁，业务逻辑出现异常，导致锁无法释放
解决：设置过期时间有两种方式：
1、首先想到通过expire设置过期时间（缺乏原子性：如果在setnx和expire之间出现异常，锁也无法释放）
2、在set时指定过期时间（推荐）
![image-20220908165634869](https://images.huangrx.cn/uploads/2022/09/08/6319ae432c5d5.png)

我们在加锁时，设置过期时间
```java
Boolean lock = redisService.setNx(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "lock"), "locked", 3, TimeUnit.SECONDS);
```
这样子测试也是没问题的，但是还存在一些问题。
问题：可能会释放其他服务器的锁。
场景：如果业务逻辑的执行时间是7s。执行流程如下

1、index1业务逻辑没执行完，3秒后锁被自动释放。
2、index2获取到锁，执行业务逻辑，3秒后锁被自动释放。
3、index3获取到锁，执行业务逻辑
4、index1业务逻辑执行完成，开始调用del释放锁，这时释放的是index3的锁，导致index3的业务只执行1s就被别人释放。

最终等于没锁的情况。
解决：setnx获取锁时，设置一个指定的唯一值（例如：uuid）；释放前获取这个值，判断是否自己的锁


####  优化 - UUID防误删
![image-20220908170813573](https://images.huangrx.cn/uploads/2022/09/08/6319b0fde7a26.png)

```java
    public void testSetNxUuidLock() {
        // 1. 从redis中获取锁,setnx， 使用uuid 防止误删除
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisService.setNx(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "lock"), uuid, 3, TimeUnit.SECONDS);
        if (lock) {
            // 查询redis中的num值
            Integer value = (Integer) redisService.get(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "num"));
            // 没有该值return
            if (Objects.isNull(value)) {
                return;
            }
            // 把redis中的num值+1
            redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "num"), ++value);

            // 2. 释放锁 del
            if (StringUtils.equals((String) redisService.get(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "lock")), uuid)) {
                redisService.del(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "lock"));
            }

        } else {
            // 3. 每隔1秒钟回调一次，再次尝试获取锁
            try {
                Thread.sleep(1000);
                testSetNxLock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
```

问题：删除操作缺乏原子性。

场景：
1、index1执行删除时，查询到的lock值确实和uuid相等
2、index1执行删除前，lock刚好过期时间已到，被redis自动释放
3、index2获取了lock
4、index1执行删除，此时会把index2的lock删除


####  优化 - LUA脚本保证删除的原子性

redis删除的LUA脚本
```shell
if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end
```

代码中调用LUA脚本
```java
    @Override
    public void testSetNxLuaLock() {
        // 1. 从redis中获取锁,setnx， 使用uuid 防止误删除
        String uuid = UUID.randomUUID().toString();
        // 2. 给锁设置过期时间，防止业务异常无法释放锁
        Boolean lock = redisService.setNx(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "lock"), uuid, 3, TimeUnit.SECONDS);
        if (lock) {
        // 查询redis中的num值
        Integer value = (Integer) redisService.get(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "num"));
        // 没有该值return
        if (Objects.isNull(value)) {
        return;
        }
        // 把redis中的num值+1
        redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "num"), ++value);

        // 3. 释放锁 del, 使用lua脚本，提供原子性（a执行删除，查询uuid相等；a setNx 获得的锁刚好到期，b进来获取到锁，导致a删除的时候，删除的是b的锁）
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        redisService.execute(script, Boolean.class, Collections.singletonList(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "setNx", "lock")), uuid);

        } else {
        // 4. 每隔1秒钟回调一次，再次尝试获取锁
        try {
        Thread.sleep(1000);
        testSetNxLock();
        } catch (InterruptedException e) {
        e.printStackTrace();
        }
        }
        }
```


#### 优化 - 可重入锁
由于上述加锁命令使用了 SETNX ，一旦键存在就无法再设置成功，这就导致后续同一线程内继续加锁，将会加锁失败。
当一个线程执行一段代码成功获取锁之后，继续执行时，又遇到加锁的子任务代码，可重入性就保证线程能继续执行，而不可重入就是需要等待锁释放之后，再次获取锁成功，才能继续往下执行。

```java
   /**
     * 可重入锁 --- 加锁
     *
     * @param lockName 锁名
     * @param uuid     标识防误删
     * @param expire   锁过期时间 单位：秒
     * @return 是否获得锁
     */
    @Override
    public Boolean tryReentrantLock(String lockName, String uuid, Long expire) {
        String script = "if (redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) == 1) " +
                "then" +
                "    redis.call('hincrby', KEYS[1], ARGV[1], 1);" +
                "    redis.call('expire', KEYS[1], ARGV[2]);" +
                "    return 1;" +
                "else" +
                "   return 0;" +
                "end";
        if (!redisService.execute(script, Boolean.class, Collections.singletonList(lockName), uuid, expire)) {
            try {
                // 没有获取到锁，重试
                Thread.sleep(200);
                tryReentrantLock(lockName, uuid, expire);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 获取到锁，返回true
        return true;
    }

    /**
     * 可重入锁 --- 解锁
     *
     * @param lockName 锁名称
     * @param uuid     标识防止误删
     */
    @Override
    public void unReentrantLock(String lockName, String uuid) {
        String script = "if (redis.call('hexists', KEYS[1], ARGV[1]) == 0) then" +
                "    return nil;" +
                "end;" +
                "if (redis.call('hincrby', KEYS[1], ARGV[1], -1) > 0) then" +
                "    return 0;" +
                "else" +
                "    redis.call('del', KEYS[1]);" +
                "    return 1;" +
                "end;";
        // 这里之所以没有跟加锁一样使用 Boolean ,这是因为解锁 lua 脚本中，三个返回值含义如下：
        // 1 代表解锁成功，锁被释放
        // 0 代表可重入次数被减 1
        // null 代表其他线程尝试解锁，解锁失败
        Long result = redisService.execute(script, Long.class, Lists.newArrayList(lockName), uuid);
        // 如果未返回值，代表尝试解其他线程的锁
        if (result == null) {
            throw new IllegalMonitorStateException("attempt to unlock lock, not locked by lockName: "
                    + lockName + " with request: " + uuid);
        }
    }
```

```java
    @RequestMapping(value = "index/testReentrantLock", method = RequestMethod.GET)
    public BaseResponse<Object> testReentrantLock() {
        // 加锁
        String uuid = UUID.randomUUID().toString();
        Boolean lock = distributedLockService.tryReentrantLock(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "REENTRANT", "LOCK"), uuid, 300L);

        if (lock) {
            // 读取redis中的num值
            Object numString = (Object) redisService.get(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "REENTRANT", "NUM"));
            if (Objects.isNull(numString)) {
                redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "REENTRANT", "NUM"), 1);
                distributedLockService.unReentrantLock(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "REENTRANT", "LOCK"), uuid);
                return BaseResponse.success(applicationContext.getEnvironment().getProperty("server.port") + "添加可重入！");
            }

            // ++操作
            Integer num = (Integer) numString;
            num++;

            // 放入redis
            redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "REENTRANT", "NUM"), String.valueOf(num));

            // 测试可重入性
            this.testSubLock(uuid);

            // 释放锁
            distributedLockService.unReentrantLock(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "REENTRANT", "LOCK"), uuid);
        }

        return BaseResponse.success(applicationContext.getEnvironment().getProperty("server.port") + "你好啊，可重入！");
    }

    /**
     * 测试可重入性<br/>
     * <br/>
     * @param uuid 重入前的锁标识，重入 入的还是这个锁
     */
    private void testSubLock(String uuid){
        // 加锁
        Boolean lock = distributedLockService.tryReentrantLock(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "REENTRANT", "LOCK"), uuid, 300L);

        if (lock) {
            System.out.println("分布式可重入锁。。。");

            distributedLockService.unReentrantLock(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_LOCK_KEY, "REENTRANT", "LOCK"), uuid);
        }
    }
```


#### 优化 - 自动续期
A线程超时时间设为10s（为了解决死锁问题），但代码执行时间可能需要30s，然后redis服务端10s后将锁删除。 此时，B线程恰好申请锁，redis服务端不存在该锁，可以申请，也执行了代码。那么问题来了， A、B线程都同时获取到锁并执行业务逻辑，这与分布式锁最基本的性质相违背：在任意一个时刻，只有一个客户端持有锁（即独享排他）。

锁延期方法：开启子线程执行延期
```java
   /**
     * 锁延期
     * 线程等待超时时间的2/3时间后,执行锁延时代码,直到业务逻辑执行完毕,因此在此过程中,其他线程无法获取到锁,保证了线程安全性
     * @param lockName 锁名称
     * @param expire 单位：毫秒
     */
    @Override
    public void renewTime(String lockName, String uuid, Long expire){
        String script = "if(redis.call('hexists', KEYS[1], ARGV[1]) == 1) then redis.call('expire', KEYS[1], ARGV[2]); return 1; else return 0; end";
        new Thread(() -> {
            while (redisService.execute(script, Boolean.class, Lists.newArrayList(lockName), uuid, expire.toString())){
                try {
                    // 到达过期时间的2/3时间，自动续期
                    Thread.sleep(expire / 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
```

```java
    @Override
    public Boolean tryReentrantLock(String lockName, String uuid, Long expire) {
        String script = "if (redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) == 1) " +
                "then" +
                "    redis.call('hincrby', KEYS[1], ARGV[1], 1);" +
                "    redis.call('expire', KEYS[1], ARGV[2]);" +
                "    return 1;" +
                "else" +
                "   return 0;" +
                "end";
        if (!redisService.execute(script, Boolean.class, Collections.singletonList(lockName), uuid, expire)) {
            try {
                // 没有获取到锁，重试
                Thread.sleep(200);
                tryReentrantLock(lockName, uuid, expire);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 锁续期, 测试自动续期时加上这个
        this.renewTime(lockName, uuid, expire * 1000);

        // 获取到锁，返回true
        return true;
    } catch
```

```java
   /**
     * 测试锁自动续期<br/>
     */
    @RequestMapping(value = "index/testLockRenewTime", method = RequestMethod.GET)
    public void testLockRenewTime() {
        // 加锁
        String uuid = UUID.randomUUID().toString();
        Boolean lock = distributedLockService.tryReentrantLock("lock", uuid, 30L);

        if (lock) {
            // 读取redis中的num值
            String numString = (String) redisService.get("num");
            if (StringUtils.isBlank(numString)) {
                return;
            }

            // ++操作
            Integer num = Integer.parseInt(numString);
            num++;

            // 放入redis
            redisService.set("num", String.valueOf(num));

            // 睡眠60s，锁过期时间30s。每隔20s自动续期
            try {
                TimeUnit.SECONDS.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 测试可重入性
            // this.testSubLock(uuid);

            // 释放锁
            distributedLockService.unReentrantLock("lock", uuid);
        }
    }
```


#### 优化 - Redlock算法
redis集群状态下的问题：

1. 客户端A从master获取到锁
2. 在master将锁同步到slave之前，master宕掉了。
3. slave节点被晋级为master节点
4. 客户端B取得了同一个资源被客户端A已经获取到的另外一个锁。

**安全失效**！

解决集群下锁失效，参照redis官方网站针对redlock文档：https://redis.io/topics/distlock

在算法的分布式版本中，我们假设有N个Redis服务器。这些节点是完全独立的，因此我们不使用复制或任何其他隐式协调系统。**前几节已经描述了如何在单个实例中安全地获取和释放锁，在分布式锁算法中，将使用相同的方法在单个实例中获取和释放锁。**将N设置为5是一个合理的值，因此需要在不同的计算机或虚拟机上运行5个Redis主服务器，确保它们以独立的方式发生故障。

为了获取锁，客户端执行以下操作：

1. 客户端以毫秒为单位获取当前时间的时间戳，作为**起始时间**。
2. 客户端尝试在所有N个实例中顺序使用相同的键名、相同的随机值来获取锁定。每个实例尝试获取锁都需要时间，客户端应该设置一个远小于总锁定时间的超时时间。例如，如果自动释放时间为10秒，则**尝试获取锁的超时时间**可能在5到50毫秒之间。这样可以防止客户端长时间与处于故障状态的Redis节点进行通信：如果某个实例不可用，尽快尝试与下一个实例进行通信。
3. 客户端获取当前时间 减去在步骤1中获得的**起始时间**，来计算**获取锁所花费的时间**。当且仅当客户端能够在大多数实例（至少3个）中获取锁时，并且获取锁所花费的总时间小于锁有效时间，则认为已获取锁。
4. 如果获取了锁，则将锁有效时间减去 **获取锁所花费的时间**，如步骤3中所计算。
5. 如果客户端由于某种原因（无法锁定N / 2 + 1个实例或有效时间为负）而未能获得该锁，它将尝试解锁所有实例（即使没有锁定成功的实例）。

每台计算机都有一个本地时钟，我们通常可以依靠不同的计算机来产生很小的时钟漂移。只有在拥有锁的客户端将在锁有效时间内（如步骤3中获得的）减去一段时间（仅几毫秒）的情况下终止工作，才能保证这一点。以补偿进程之间的时钟漂移


当客户端无法获取锁时，它应该在随机延迟后重试，以避免同时获取同一资源的多个客户端之间不同步（这可能会导致脑裂的情况：没人胜）。同样，客户端在大多数Redis实例中尝试获取锁的速度越快，出现裂脑情况（以及需要重试）的窗口就越小，因此理想情况下，客户端应尝试将SET命令发送到N个实例同时使用多路复用。

值得强调的是，对于未能获得大多数锁的客户端，尽快释放（部分）获得的锁有多么重要，这样就不必等待锁定期满才能再次获得锁（但是，如果发生了网络分区，并且客户端不再能够与Redis实例进行通信，则在等待密钥到期时需要付出可用性损失）。



#### 总结
分布式锁三个操作：
1、加锁
2、解锁
3、重试

为了确保分布式锁可用，我们至少要确保锁的实现同时满足以下几个条件：

- 互斥性。在任意时刻，只有一个客户端能持有锁。
- 不会发生死锁。即使有一个客户端在持有锁的期间崩溃而没有主动解锁，也能保证后续其他客户端能加锁。
- 解铃还须系铃人。加锁和解锁必须是同一个客户端，客户端自己不能把别人加的锁给解了。
- 加锁和解锁必须具有原子性。
- 实现可重入锁（可选）
- 为了防止锁失效，锁要具备自动续期
- 防止集群情况下锁失效，可以使用Redlock


### 分布式锁之Redisson

Redisson是一个在Redis的基础上实现的Java驻内存数据网格（In-Memory Data Grid）。
它不仅提供了一系列的分布式的Java常用对象，还提供了许多分布式服务。
其中包括(BitSet, Set, Multimap, SortedSet, Map, List, Queue, BlockingQueue, Deque, BlockingDeque, Semaphore, Lock, AtomicLong, CountDownLatch, Publish / Subscribe, Bloom filter, Remote service, Spring cache, Executor service, Live Object service, Scheduler service) Redisson提供了使用Redis的最简单和最便捷的方法。
Redisson的宗旨是促进使用者对Redis的关注分离（Separation of Concern），从而让使用者能够将精力更集中地放在处理业务逻辑上。

官方文档地址：https://github.com/redisson/redisson/wiki


#### 快速入门

1、引入依赖

```xml
<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson</artifactId>
    <version>3.11.2</version>
</dependency>
```


2、添加配置

```java
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        // 可以用"rediss://"来启用SSL连接
        config.useSingleServer().setAddress("redis://172.16.116.100:6379");
        return Redisson.create(config);
    }
}
```


3、代码实现

```java
@Controller
public class RedissonController {
    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedisService redisService;

    @Resource
    private ApplicationContext applicationContext;

    @RequestMapping(value = "/redisson/testLock", method = RequestMethod.GET)
    public void testLock() {

        System.out.println("redisson test lock: " + applicationContext.getEnvironment().getProperty("server.port"));

        // 只要锁的名称相同就是同一把锁
        RLock lock = this.redissonClient.getLock(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_REDISSON_KEY, "REDISSON", "LOCK"));
        // 加锁
        lock.lock();

        // 查询redis中的num值
        Object value = redisService.get(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_REDISSON_KEY, "REDISSON", "NUM"));
        // 没有该值return
        if (Objects.isNull(value)) {
            redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_REDISSON_KEY, "REDISSON", "NUM"), 1);
            lock.unlock();
            return;
        }
        // 有值就转成成int
        int num =Integer.parseInt(value.toString());
        // 把redis中的num值+1
        redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_REDISSON_KEY, "REDISSON", "NUM"), String.valueOf(++num));
        // 解锁
        lock.unlock();
    }
}
```

使用ab压力测试，查看redis内容：
![image-20220914094507740](https://images.huangrx.cn/uploads/2022/09/14/6321322bcc65d.png)



#### 可重入锁（Reentrant Lock）

基于Redis的Redisson分布式可重入锁`RLock` Java对象实现了`java.util.concurrent.locks.Lock`接口。

大家都知道，如果负责储存这个分布式锁的Redisson节点宕机以后，而且这个锁正好处于锁住的状态时，这个锁会出现锁死的状态。为了避免这种情况的发生，Redisson内部提供了一个监控锁的看门狗，它的作用是在Redisson实例被关闭前，不断的延长锁的有效期。默认情况下，看门狗检查锁的超时时间是30秒钟，也可以通过修改`Config.lockWatchdogTimeout`来另行指定。

另外Redisson还通过加锁的方法提供了`leaseTime`的参数来指定加锁的时间。超过这个时间后锁便自动解开了。



快速入门使用的就是可重入锁。也是最常使用的锁。



最常见的使用：

```java
RLock lock = redisson.getLock("anyLock");
// 最常使用
lock.lock();


// 加锁以后10秒钟自动解锁
// 无需调用unlock方法手动解锁
lock.lock(10, TimeUnit.SECONDS);


// 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
if (res) {
   try {
     ...
   } finally {
       lock.unlock();
   }
}
```


改造程序：
```java
    @RequestMapping(value = "/redisson/testAutomaticReleaseLock", method = RequestMethod.GET)
    public BaseResponse<String> testAutomaticReleaseLock() {

        System.out.println("redisson test lock: " + applicationContext.getEnvironment().getProperty("server.port"));

        // 只要锁的名称相同就是同一把锁
        RLock lock = this.redissonClient.getLock(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_REDISSON_KEY, "REDISSON", "LOCK"));
        // 加锁
        lock.lock(10, TimeUnit.SECONDS);

        // 查询redis中的num值
        Object value = redisService.get(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_REDISSON_KEY, "REDISSON", "NUM"));
        // 没有该值return
        if (Objects.isNull(value)) {
            redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_REDISSON_KEY, "REDISSON", "NUM"), 1);
            return BaseResponse.success("结束 ～～～！");
        }
        // 有值就转成成int
        int num =Integer.parseInt(value.toString());
        // 把redis中的num值+1
        redisService.set(CacheConstants.generateKey(CacheConstants.DISTRIBUTED_REDISSON_KEY, "REDISSON", "NUM"), String.valueOf(++num));
        return BaseResponse.success("结束 ～～～！");
    }
```

重启后在浏览器测试：
![image-20220914101216150](https://images.huangrx.cn/uploads/2022/09/14/63213880bc5e7.png)

在这10s期间，可以在redis客户端看到lock锁的内容：
![image-20220914101320671](https://images.huangrx.cn/uploads/2022/09/14/632138c12fef4.png)



#### 读写锁（ReadWriteLock）

基于Redis的Redisson分布式可重入读写锁`RReadWriteLock` Java对象实现了`java.util.concurrent.locks.ReadWriteLock`接口。其中读锁和写锁都继承了RLock接口。

分布式可重入读写锁允许同时有多个读锁和一个写锁处于加锁状态。

```java
RReadWriteLock rwlock = redisson.getReadWriteLock("anyRWLock");
// 最常见的使用方法
rwlock.readLock().lock()/unlock();;
// 或
rwlock.writeLock().lock()/unlock();;


// 10秒钟以后自动解锁
// 无需调用unlock方法手动解锁
rwlock.readLock().lock(10, TimeUnit.SECONDS);
// 或
rwlock.writeLock().lock(10, TimeUnit.SECONDS);

// 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
boolean res = rwlock.readLock().tryLock(100, 10, TimeUnit.SECONDS);
// 或
boolean res = rwlock.writeLock().tryLock(100, 10, TimeUnit.SECONDS);
...
```



IndexController中的两个方法：

```java
@GetMapping("index/read")
@ResponseBody
public ResponseVo<String> read(){
    String msg = indexService.readLock();

    return ResponseVo.ok(msg);
}

@GetMapping("index/write")
@ResponseBody
public ResponseVo<String> write(){
    String msg = indexService.writeLock();

    return ResponseVo.ok(msg);
}
```

IndexService接口方法：**注意保证锁的名称一致，才能使用同一把锁**

```java
public String readLock() {
    // 初始化读写锁
    RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("readwriteLock");
    RLock rLock = readWriteLock.readLock(); // 获取读锁

    rLock.lock(10, TimeUnit.SECONDS); // 加10s锁

    String msg = this.redisTemplate.opsForValue().get("msg");

    //rLock.unlock(); // 解锁
    return msg;
}

public String writeLock() {
    // 初始化读写锁
    RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("readwriteLock");
    RLock rLock = readWriteLock.writeLock(); // 获取写锁

    rLock.lock(10, TimeUnit.SECONDS); // 加10s锁

    this.redisTemplate.opsForValue().set("msg", UUID.randomUUID().toString());

    //rLock.unlock(); // 解锁
    return "成功写入了内容。。。。。。";
}
```



打开开两个浏览器窗口测试：

- 同时访问写：一个写完之后，等待一会儿（约10s），另一个写开始

- 同时访问读：不用等待

- 先写后读：读要等待（约10s）写完成

- 先读后写：写要等待（约10s）读完成



#### 信号量（Semaphore）和闭锁（CountDownLatch）

基于Redis的Redisson的分布式信号量（Semaphore）Java对象`RSemaphore`采用了与`java.util.concurrent.Semaphore`相似的接口和用法。

```java
RSemaphore semaphore = redisson.getSemaphore("semaphore");
semaphore.acquire();
//或
semaphore.acquire(23);
semaphore.tryAcquire();
semaphore.tryAcquire(23, TimeUnit.SECONDS);

// 释放资源
semaphore.release();
```



基于Redisson的Redisson分布式闭锁（CountDownLatch）Java对象`RCountDownLatch`采用了与`java.util.concurrent.CountDownLatch`相似的接口和用法。

```java
// 在阻塞线程中
RCountDownLatch latch = redisson.getCountDownLatch("anyCountDownLatch");
latch.trySetCount(1);
latch.await(); //阻塞线程，等初始计数减为0时，才放行

// 在其他线程或其他JVM里
RCountDownLatch latch = redisson.getCountDownLatch("anyCountDownLatch");
latch.countDown();//每执行一次减一
```

需要两个线程，一个等待。一个计数countDown



**演示代码**

IndexController：

```java
/**
     * 等待
     * @return
     */
@GetMapping("index/latch")
@ResponseBody
public ResponseVo<Object> countDownLatch(){

    String msg = indexService.latch();

    return ResponseVo.ok(msg);
}

/**
  * 计数
  * @return
  */
@GetMapping("out")
@ResponseBody
public ResponseVo<Object> out(){

    String msg = indexService.countDown();

    return ResponseVo.ok(msg);
}
```

IndexService：

```java
public String latch() {
    RCountDownLatch countDownLatch = this.redissonClient.getCountDownLatch("countdown");
    try {
        countDownLatch.trySetCount(6);
        countDownLatch.await();

        return "关门了。。。。。";
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    return null;
}

public String countDown() {
    RCountDownLatch countDownLatch = this.redissonClient.getCountDownLatch("countdown");

    countDownLatch.countDown();
    return "出来了一个人。。。";
}
```



重启测试，打开两个页面：当第二个请求执行6次之后，第一个请求才会执行。

![1586070347231](../../12-笔记图集/1586070347231-redis.png)



### 分布式锁 + AOP实现缓存

随着业务中缓存及分布式锁的加入，业务代码变的复杂起来，除了需要考虑业务逻辑本身，还要考虑缓存及分布式锁的问题，增加了程序员的工作量及开发难度。而缓存的玩法套路特别类似于事务，而声明式事务就是用了aop的思想实现的。

![img](https://images.huangrx.cn/uploads/2022/09/14/63213a5804013.png)

1. 以 @Transactional 注解为植入点的切点，这样才能知道@Transactional注解标注的方法需要被代理。
2. @Transactional注解的切面逻辑类似于@Around

模拟事务，缓存可以这样实现：

1. 自定义缓存注解@HuangrxCache（类似于事务@Transactional）
2. 编写切面类，使用环绕通知实现缓存的逻辑封装

![image-20220914103532747](https://images.huangrx.cn/uploads/2022/09/14/63213dfd9fa6f.png)

定义一个注解：GmallCache

```java
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GmallCache {

    /**
     * 缓存的前缀
     * @return
     */
    String prefix() default "";

    /**
     * 设置缓存的有效时间
     * 单位：分钟
     * @return
     */
    int timeout() default 5;

    /**
     * 防止雪崩设置的随机值范围
     * @return
     */
    int random() default 5;

    /**
     * 防止击穿，分布式锁的key
     * @return
     */
    String lock() default "lock";
}
```

定义一个切面类加强注解：

```java
package com.huangrx.huangrx.redis.config;

import com.alibaba.fastjson.JSON;
import com.huangrx.huangrx.redis.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 缓存切面
 *
 * @author        hrenxiang
 * @since         2022-09-14 10:23:45
 */
@Aspect
@Component
public class HuangrxCacheAspect {

    @Resource
    private RedisService redisService;

    @Resource
    private RedissonClient redissonClient;

    /**
     * joinPoint.getArgs(); 获取方法参数
     * joinPoint.getTarget().getClass(); 获取目标类
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.huangrx.huangrx.redis.config.HuangrxCache)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        // 获取切点方法的签名
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        // 获取方法对象
        Method method = signature.getMethod();
        // 获取方法上指定注解的对象
        HuangrxCache annotation = method.getAnnotation(HuangrxCache.class);
        // 获取注解中的前缀
        String prefix = annotation.prefix();
        // 获取方法的参数
        Object[] args = joinPoint.getArgs();
        String param = Arrays.asList(args).toString();
        // 获取方法的返回值类型
        Class<?> returnType = method.getReturnType();

        // 拦截前代码块：判断缓存中有没有
        Object json = redisService.get(prefix + param);
        // 判断缓存中的数据是否为空
        if (Objects.nonNull(json)){
            return JSON.parseObject(json.toString(), returnType);
        }

        // 没有，加分布式锁
        String lock = annotation.lock();
        RLock rLock = this.redissonClient.getLock(lock + param);
        rLock.lock();

        // 判断缓存中有没有，有直接返回(加锁的过程中，别的请求可能已经把数据放入缓存)
        Object json2 = redisService.get(prefix + param);
        // 判断缓存中的数据是否为空
        if (Objects.nonNull(json2)){
            rLock.unlock();
            return JSON.parseObject(json2.toString(), returnType);
        }

        // 执行目标方法
        Object result = joinPoint.proceed(joinPoint.getArgs());

        // 拦截后代码块：放入缓存 释放分布锁
        int timeout = annotation.timeout();
        int random = annotation.random();
        redisService.set(prefix + param, JSON.toJSONString(result), timeout + new Random().nextInt(random), TimeUnit.MINUTES);
        rLock.unlock();

        return result;
    }
}
```



CacheAnnotationsController，使用缓存注解完成数据缓存功能：

```java
package com.huangrx.huangrx.redis.controller;

import com.huangrx.huangrx.redis.config.HuangrxCache;
import com.huangrx.huangrx.redis.model.entity.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hrenxiang
 * @since 2022-09-14 10:36
 */
@RestController
public class CacheAnnotationsController {

    @HuangrxCache(prefix = "CACHE:PERSONLIST:", timeout = 14400, random = 3600, lock = "lock")
    @RequestMapping(value = "/cache/queryPersonList", method = RequestMethod.GET)
    public List<Person> queryPersonList(@RequestParam("pid") Long pid) {
        System.out.println("没有走缓存哦！！！！！！");
        List<Person> personList = new ArrayList<>();
        Person john = Person.builder()
                .name("John")
                .email("john@example.com")
                .pid("111")
                .build();
        personList.add(john);

        Person alisa = Person.builder()
                .name("alisa")
                .email("alisa@example.com")
                .pid("333")
                .build();
        personList.add(alisa);

        Person nn = Person.builder()
                .name("nn")
                .email("nn@example.com")
                .pid("444")
                .build();
        personList.add(nn);

        Person vv = Person.builder()
                .name("xx")
                .email("xx@example.com")
                .pid("555")
                .build();
        personList.add(vv);

        Person bb = Person.builder()
                .name("bb")
                .email("bb@example.com")
                .pid("666")
                .build();
        personList.add(bb);

        Person dd = Person.builder()
                .name("dd")
                .email("dd@example.com")
                .pid("777")
                .build();
        personList.add(dd);

        Person ww = Person.builder()
                .name("ww")
                .email("ww@example.com")
                .pid("888")
                .build();
        personList.add(ww);

        Person hh = Person.builder()
                .name("hh")
                .email("hh@example.com")
                .pid("999")
                .build();
        personList.add(hh);

        return personList;
    }
}

```

该方法的实现只需要考虑业务逻辑本身，使用注解即可完成缓存功能。


测试：
![image-20220914105803088](https://images.huangrx.cn/uploads/2022/09/14/6321433bc3eb5.png)

访问两次pid为5 的数据
http://localhost:8081/cache/queryPersonList?pid=5

第一次打印，说明没有走缓存
![image-20220914105955769](https://images.huangrx.cn/uploads/2022/09/14/632143ae59e63.png)

第二次没有打印，说明走了缓存，我们的缓存切面是成功的