package com.huangrx.huangrx.redis.service;

/**
 * @author hrenxiang
 * @since 2022-09-08 10:26
 */
public interface DistributedLockService {
    /**
     * 测试
     */
    void testLock();

    /**
     * 测试本地锁
     */
    void testSynchronizedLock();

    /**
     * 分布式锁基础版<br/>
     * <br/>
     * 后续修改了一下，给redis缓存的key（锁）添加过期时间，防止死锁（无法释放锁）
     */
    void testSetNxLock();

    /**
     * UUID 标识防止误删<br/>
     *<br/>
     * 场景：<br/>
     * a业务逻辑没执行完， 3秒后自动释放（已经释放）<br/>
     * b业务获取到锁   ， 3秒后自动释放（已经释放）<br/>
     * c业务获取到锁   ， 执行业务逻辑，假设此时c才执行1秒（还未释放）<br/>
     * 同时业务a执行完逻辑，进行del释放锁，释放的是 c业务的锁，这时的业务c等于根本没加锁<br/>
     * <br/>
     * 解决：<br/>
     * setnx获取锁时，设置一个指定的唯一值（例如：uuid）；释放前获取这个值，判断是否自己的锁<br/>
     */
    void testSetNxUuidLock();

    /**
     * LUA 脚本保证原子性<br/>
     * <br/>
     * 1. a业务执行删除时，查询到的锁lock对应的值确实和uuid相等 (a的锁未删除，但还未执行删除操作)<br/>
     * 2. a业务执行删除前，lock刚好过期时间已到，被redis自动释放（a的锁被自动释放，但还未执行删除操作）<br/>
     * 3. b业务获取到锁（此时是b的锁）<br/>
     * 4. a业务执行删除，此时会把b业务的锁删除（a业务进行删除操作）<br/>
     *<br/>
     * 解决：<br/>
     * LUA脚本，保证删除的原子性<br/>
     * redis 的官方文档中有描述lua脚本在执行的时候具有排他性，不允许其他命令或者脚本执行，类似于事务。<br/>
     * 但是存在的另一个问题是，它在执行的过程中如果一个命令报错不会回滚已执行的命令，所以要保证lua脚本的正确性<br/>
     * 有两个运行的函数call()和pcall(),两者区别在于，call在执行命令的时候如果报错，会抛出reisd错误终止执行，而pcall不会中断会记录下来错误信息<br/>
     */
    void testSetNxLuaLock();

    /**
     * 可重入锁 --- 加锁 <br/>
     * @param lockName 锁名<br/>
     * @param uuid 标识防误删<br/>
     * @param expire 锁过期时间<br/>
     * @return 是否获得锁<br/>
     */
    Boolean tryReentrantLock(String lockName, String uuid, Long expire);

    /**
     * 可重入锁 --- 解锁<br/>
     * @param lockName 锁名称<br/>
     * @param uuid 标识防止误删<br/>
     */
    void unReentrantLock(String lockName, String uuid);

    /**
     * 自动续期<br/>
     * 线程等待超时时间的2/3时间后,执行锁延时代码,直到业务逻辑执行完毕,因此在此过程中,其他线程无法获取到锁,保证了线程安全性<br/>
     * @param lockName 锁名称<br/>
     * @param uuid 标识，给相应标识的锁续期<br/>
     * @param expire 单位：毫秒<br/>
     */
    void renewTime(String lockName, String uuid, Long expire);
}
