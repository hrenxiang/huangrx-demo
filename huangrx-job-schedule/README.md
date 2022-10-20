### 简介

Spring 为任务调度和基于使用 @Scheduled 注释的 cron 表达式的异步方法执行提供了极好的支持。

可以将@Scheduled 注释与触发器元数据一起添加到方法中。

### @EnableScheduling

启用定时任务

#### @Component

如果配置了定时任务，则类上一定要 添加 @component 注解，不然扫描不到

```java

@Slf4j
@Component
public class OneSecondsTask {

    @Scheduled(fixedDelay = 1000)
    public void runScheduleFixedRate() {
        log.info("runScheduleFixedRate: current DateTime, {}", LocalDateTime.now());
    }
}
```

### @Schedule注解一览

@Scheduled注解共有8个属性（其中有3组只是不同类型的相同配置）和一个常量CRON_DISABLED，源码如下：

```java
/**
 * 标记要调度的方法的注释。 必须准确指定 cron、fixedDelay 或 fixedRate 属性之一。带注释的方法必须没有参数。    
 * 它通常有一个 void 返回类型； 如果不是，则在通过调度程序调用时将忽略返回值。@Scheduled 注解的处理是通过注册
 * 一个 ScheduledAnnotationBeanPostProcessor 来执行的。 这可以手动完成，或者更方便的是，通过 
 * <task:annotation-driven/> 元素或 @EnableScheduling 注释。
 * 此注释可用作元注释以创建具有属性覆盖的自定义组合注释。
 * @since 3.0
 * @see EnableScheduling
 * @see ScheduledAnnotationBeanPostProcessor
 * @see Schedules
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(Schedules.class)
public @interface Scheduled {

    /**
     * 指示禁用触发器的特殊 cron 表达式值：“-”。
     * 这主要用于 ${...} 占位符，允许外部禁用相应的计划方法。
     *
     * @since 5.1
     * @see ScheduledTaskRegistrar#CRON_DISABLED
     */
    String CRON_DISABLED = ScheduledTaskRegistrar.CRON_DISABLED;

    /**
     * 一个类似 cron 的表达式，扩展了通常的 UN*X 定义以包括秒、分、小时、月中的某一天、月份和星期几的触发器。
     * 例如，“0 * * * * MON-FRI”表示工作日每分钟一次（在分钟的顶部 - 第 0 秒）。
     *
     * 从左到右读取的字段解释如下：
     * <ul>
     * <li>second</li>
     * <li>minute</li>
     * <li>hour</li>
     * <li>day of month</li>
     * <li>month</li>
     * <li>day of week</li>
     * </ul>
     * 特殊值“-”表示禁用的 cron 触发器，主要用于由 ${...} 占位符解析的外部指定值。
     * @return 返回一个可以解析为 cron 计划的表达式
     * @see org.springframework.scheduling.support.CronSequenceGenerator
     */
    String cron() default "";

    /**
     * 将解析 cron 表达式的时区。 默认:""（即使用服务器的本地时区）。
     *
     * @return java.util.TimeZone.getTimeZone(String) 接受的区域 ID，或指示服务器默认时区的空字符串
     * @since 4.0
     * @see org.springframework.scheduling.support.CronTrigger#CronTrigger(String, java.util.TimeZone)
     * @see java.util.TimeZone
     */
    String zone() default "";

    /**
     * 在上次调用结束和下一次调用开始之间的固定时间段，单位：毫秒，默认：-1(不延迟)
     *
     * @return 延迟时长，单位：毫秒
     */
    long fixedDelay() default -1;

    /**
     * 上次调用的结束和下一次调用的开始之间固定时间间隔字符串，单位：毫秒。 
     *
     * @return 延迟值字符串，单位：毫秒，例如占位符或
     *         {@link java.time.Duration#parse java.time.Duration} 兼容值
     * @since 3.2.2
     */
    String fixedDelayString() default "";

    /**
     * 在调用之间的固定时间段，单位：毫秒。
     *
     * @return 以毫秒为单位的周期
     */
    long fixedRate() default -1;

    /**
     * 在调用之间的固定时间段字符串，单位：毫秒。
     *
     * @return 延迟值字符串，单位：毫秒，例如占位符或 
     *         {@link java.time.Duration#parse java.time.Duration} 兼容值
     * @since 3.2.2
     */
    String fixedRateString() default "";

    /**
     * 在第一次执行 fixedRate 或 fixedDelay 任务之前延迟的毫秒数。
     *
     * @return 初始延迟值，单位：毫秒
     * @since 3.2
     */
    long initialDelay() default -1;

    /**
     * 在第一次执行 fixedRate 或 fixedDelay 任务之前延迟的毫秒数字符串。
     * @return 初始延迟值字符串，单位：毫秒，例如占位符或符合 java.time.Duration 的值
     * @since 3.2.2
     */
    String initialDelayString() default "";

}

```

### 参数详解

#### 1. cron

该参数接收一个 cron 表达式字符串，字符串以 5 或 6 个空格隔开，分开共 6 或 7 个域，每一个域代表一个含义,，其顺序和含义见对应注释说明，如下：

```
[秒] [分] [小时] [日] [月] [周] [年]
```

其中，[年]不是必须的域，可以省略 [年]，省略[年] 则一共 6 个域。

| 域   | 必填 | 值的范围       | 允许的通配符  |
| ---- | ---- | -------------- | ------------- |
| 秒   | 是   | 0-59           | , - * /       |
| 分   | 是   | 0-59           | , - * /       |
| 时   | 是   | 0-23           | , - * /       |
| 日   | 是   | 1-31           | , - * ? / L W |
| 月   | 是   | 1-12 / JAN-DEC | , - * /       |
| 周   | 是   | 1-7 or SUN-SAT | , - * ? / L # |
| 年   | 否   | 1970-2099      | , - * /       |

**TIPS**

```yaml
task:
  # 每天凌晨2点执行任务
  cron: 0 0 2 * * ?
  # 任务间隔分钟数
  interval: 15
  # 在上次调用结束和下一次调用开始的间隔时间
  fixed-delay: 5000
```

任务代码方法：

```java
/**
 * 测试配置的 cron 表达式
 */
@Scheduled(cron = "${task.cron}")
public void test_1(){
        System.out.println("Task running: "+System.currentTimeMillis());
        }
/**
 * 测试分钟配置表达式
 */
@Scheduled(cron = "* 0/${task.interval} * * * *")
 void test_2(){
         System.out.println("Task running: "+System.currentTimeMillis());
         }
```

#### 2. zone

时区，接收一个`java.util.TimeZone#ID`。

cron 表达式会基于该时区解析。

默认是一个空字符串，即取服务器所在地的时区。

比如我们一般使用的时区 Asia/Shanghai。该字段我们一般留空。

#### 3. fixedDelay / fixedDelayString

上一次执行完毕时间点之后多长时间再执行，`long`型。如：

```java
// 上次任务执行结束后15秒再执行下一次
@Scheduled(fixedDelay = 15000)
```

`fixedDelayString`与 `fixedDelay`意思相同，`String`类型。唯一不同是**支持占位符**。如：

```java
// 上次任务执行结束后15秒再执行下一次
@Scheduled(fixedDelayString = "15000")
```

占位符的使用 (上文中有配置：task.fixed-delay=15000)：

```java
@Scheduled(fixedDelayString = "${task.fixed-delay}")
 void testFixedDelayString(){
         System.out.println("Task running: "+System.currentTimeMillis());
         }
```

#### 4. fixedRate / fixedRateString

上一次开始执行时间点之后多长时间再执行，`long`型。如：

```java
// 上次任务执行结束后15秒再执行下一次
@Scheduled(fixedRate = 15000)
```

`fixedRateString`与 `fixedRate` 意思相同，`String`类型。唯一不同是**支持占位符**。

#### 5. initialDelay / initialDelayString

第一次延迟多长时间后再执行，`long`型。如：

```java
//  第一次延迟5秒后执行，之后按fixedRate的规则每10秒执行一次
@Scheduled(initialDelay = 5000, fixedRate = 10000)
```

`initialDelayString`与 `initialDelay `意思相同，`String`类型。唯一不同是**支持占位符**。

### 自定义配置，可有可无

```java
/**
 *
 */
package com.huangrx.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * from:
 * <p>
 * {@link org.springframework.scheduling.annotation.AbstractAsyncConfiguration} setConfigurers
 * </p>
 * <p>
 * {@link org.springframework.scheduling.annotation.ProxyAsyncConfiguration}
 * </p>
 *
 * @author JerryXia
 *
 */
@EnableScheduling
@Configuration
public class ApplicationSchedulerConfiguration implements SchedulingConfigurer {
    private static final Logger log = LoggerFactory.getLogger(ApplicationSchedulerConfiguration.class);

    /**
     * for spring scheduling, beanName: taskScheduler
     *
     * @return org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
     */
    @Bean(name = ScheduledAnnotationBeanPostProcessor.DEFAULT_TASK_SCHEDULER_BEAN_NAME, destroyMethod = "destroy")
    public ThreadPoolTaskScheduler taskScheduler() {
        if (log.isDebugEnabled()) {
            log.debug("Initializing ExecutorService 'taskScheduler' -> begin constructor");
        }
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(availableProcessors);
        scheduler.setThreadNamePrefix("task-scheduler-");
        return scheduler;
    }

    /**
     * for spring scheduling, ScheduledAnnotationBeanPostProcessor.finishRegistration
     *
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (log.isDebugEnabled()) {
            log.debug("configure Tasks -> begin");
        }
        if (taskRegistrar.getScheduler() == null) {
            taskRegistrar.setScheduler(taskScheduler());
        }
        if (log.isDebugEnabled()) {
            log.debug("configure Tasks -> end");
        }
    }
}
```

### 使用 Spring Schedule 要注意什么

#### 关于异常处理

建议自行处理异常

#### 关于超时处理

在实际的开发中，这个问题经常会出现，比如执行一段时间后定时任务不再执行了；
这种情况会发生在，比如你调用一个第三方接口，没有设置调用超时，继而引发异常，这时候当前任务便阻塞了。

### SpringTask 的原理

#### @EnableScheduling 注解

添加 @EnableScheduling 注解会自动注入 SchedulingConfiguration

```java
 *@author Chris Beams
        *@author Juergen Hoeller
        *@since 3.1
        *@see Scheduled
        *@see SchedulingConfiguration
        *@see SchedulingConfigurer
        *@see ScheduledTaskRegistrar
        *@see Trigger
        *@see ScheduledAnnotationBeanPostProcessor
        */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SchedulingConfiguration.class)
@Documented
public @interface EnableScheduling {

}

```

#### SchedulingConfiguration 中初始化 ScheduledAnnotationBeanPostProcessor

SchedulingConfiguration 配置中自动初始化 ScheduledAnnotationBeanPostProcessor

```java

@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class SchedulingConfiguration {

    @Bean(name = TaskManagementConfigUtils.SCHEDULED_ANNOTATION_PROCESSOR_BEAN_NAME)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ScheduledAnnotationBeanPostProcessor scheduledAnnotationProcessor() {
        return new ScheduledAnnotationBeanPostProcessor();
    }

}

```

什么是 BeanPostProcessor?
我们在前文中有详解的讲解，具体看[Spring 核心之控制反转 (IOC) 源码解析](https://www.pdai.tech/md/spring/spring-x-framework-ioc-source-3.html)

**Spring 容器中 Bean 的生命周期流程**
![image-20221020103813528](https://images.huangrx.cn/uploads/2022/10/20/6350b495d32f0.png)

#### ScheduledTaskRegistrar 注册 task

在 ScheduledAnnotationBeanPostProcessor 构造函数中初始化了 ScheduledTaskRegistrar

```Plain Text
public ScheduledAnnotationBeanPostProcessor() {
    this.registrar = new ScheduledTaskRegistrar();
}

```

先看下 ScheduledAnnotationBeanPostProcessor中有什么属性

```java
public static final String DEFAULT_TASK_SCHEDULER_BEAN_NAME="taskScheduler";

//定时任务的注册器,通过这个类将定时任务委托给定时任务线程池
private final ScheduledTaskRegistrar registrar;

//定时任务线程池,如果不为空使用这个scheduler当作ScheduledTaskRegistrar的线程池
@Nullable
private Object scheduler;

//string属性解析器,用来解析${}对应的配置文件的属性,aware接口注入
@Nullable
private StringValueResolver embeddedValueResolver;

@Nullable
private String beanName;

//aware接口注入
@Nullable
private BeanFactory beanFactory;

//aware接口注入
@Nullable
private ApplicationContext applicationContext;

//已检测的没有scheduled注解的类的集合
private final Set<Class<?>>nonAnnotatedClasses=Collections.newSetFromMap(new ConcurrentHashMap<>(64));

//保存class与scheduled方法的映射
private final Map<Object, Set<ScheduledTask>>scheduledTasks=new IdentityHashMap<>(16);
```

ScheduledTaskRegistrar 最主要的是注册各种类型的 task （这种方式在新的版本中已经废弃了）

```java
protected void scheduleTasks() {
    if (this.taskScheduler == null) {
        this.localExecutor = Executors.newSingleThreadScheduledExecutor();
        this.taskScheduler = new ConcurrentTaskScheduler(this.localExecutor);
    }
    if (this.triggerTasks != null) {
        for (TriggerTask task : this.triggerTasks) {
            addScheduledTask(scheduleTriggerTask(task));
        }
    }
    if (this.cronTasks != null) {
        for (CronTask task : this.cronTasks) {
            addScheduledTask(scheduleCronTask(task));
        }
    }
    if (this.fixedRateTasks != null) {
        for (IntervalTask task : this.fixedRateTasks) {
            addScheduledTask(scheduleFixedRateTask(task));
        }
    }
    if (this.fixedDelayTasks != null) {
        for (IntervalTask task : this.fixedDelayTasks) {
            addScheduledTask(scheduleFixedDelayTask(task));
        }
    }
}
```

注册哪些 Task，怎么设计类的呢？

![image](https://images.huangrx.cn/uploads/2022/10/20/6350ae4cc9274.png)



#### ScheduledAnnotationBeanPostProcessor 加载 Scheduled 注解

在 BeanPostProcessor 的 postProcessAfterInitialization 阶段加载 Scheduled 注解

```java
@Override
public Object postProcessAfterInitialization(Object bean,String beanName){
    if(bean instanceof AopInfrastructureBean||bean instanceof TaskScheduler||
            bean instanceof ScheduledExecutorService){
        // Ignore AOP infrastructure such as scoped proxies.
        return bean;
    }

    Class<?> targetClass=AopProxyUtils.ultimateTargetClass(bean);
    if(!this.nonAnnotatedClasses.contains(targetClass)&&
            AnnotationUtils.isCandidateClass(targetClass,Arrays.asList(Scheduled.class,Schedules.class))){
        Map<Method, Set<Scheduled>>annotatedMethods=MethodIntrospector.selectMethods(targetClass,
                (MethodIntrospector.MetadataLookup<Set<Scheduled>>)method->{
                    Set<Scheduled> scheduledMethods=AnnotatedElementUtils.getMergedRepeatableAnnotations(
                            method,Scheduled.class,Schedules.class);
                    return(!scheduledMethods.isEmpty()?scheduledMethods:null);
                });
        if(annotatedMethods.isEmpty()){
            this.nonAnnotatedClasses.add(targetClass);
            if(logger.isTraceEnabled()){
                logger.trace("No @Scheduled annotations found on bean class: "+targetClass);
            }
        }else{
            // Non-empty set of methods
            annotatedMethods.forEach((method,scheduledMethods)->
                    scheduledMethods.forEach(scheduled->processScheduled(scheduled,method,bean)));
            if(logger.isTraceEnabled()){
                logger.trace(annotatedMethods.size()+" @Scheduled methods processed on bean '"+beanName+
                        "': "+annotatedMethods);
            }
        }
    }
    return bean;
}
```

Scheduled 注解是添加到方法级别，具体如下

```java

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(Schedules.class)
public @interface Scheduled {


    String CRON_DISABLED = ScheduledTaskRegistrar.CRON_DISABLED;


    String cron() default "";


    String zone() default "";


    long fixedDelay() default -1;


    String fixedDelayString() default "";


    long fixedRate() default -1;


    String fixedRateString() default "";


    long initialDelay() default -1;


    String initialDelayString() default "";


    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

}

```

@Scheduled 所支持的参数：
1. `cron`：cron 表达式，指定任务在特定时间执行；
2. `fixedDelay`：表示上一次任务执行完成后多久再次执行，参数类型为 long，单位 ms；
3. `fixedDelayString`：与 fixedDelay 含义一样，只是参数类型变为 String；
4. `fixedRate`：表示按一定的频率执行任务，参数类型为 long，单位 ms；
5. `fixedRateString`: 与 fixedRate 的含义一样，只是将参数类型变为 String；
6. `initialDelay`：表示延迟多久再第一次执行任务，参数类型为 long，单位 ms；
7. `initialDelayString`：与 initialDelay 的含义一样，只是将参数类型变为 String；
8. `zone`：时区，默认为当前时区，一般没有用到。



获取到方法上 Scheduled 注解（对任务的定义），通过 processScheduled 处理具体类型的 task
```java
protected void processScheduled(Scheduled scheduled,Method method,Object bean){
    try{
        Runnable runnable=createRunnable(bean,method);
        boolean processedSchedule=false;
        String errorMessage=
                "Exactly one of the 'cron', 'fixedDelay(String)', or 'fixedRate(String)' attributes is required";

        Set<ScheduledTask> tasks=new LinkedHashSet<>(4);


        long initialDelay=convertToMillis(scheduled.initialDelay(),scheduled.timeUnit());
        String initialDelayString=scheduled.initialDelayString();
        if(StringUtils.hasText(initialDelayString)){
            Assert.isTrue(initialDelay< 0,"Specify 'initialDelay' or 'initialDelayString', not both");
            if(this.embeddedValueResolver!=null){
                initialDelayString=this.embeddedValueResolver.resolveStringValue(initialDelayString);
            }
            if(StringUtils.hasLength(initialDelayString)){
                try{
                    initialDelay=convertToMillis(initialDelayString,scheduled.timeUnit());
                }
                catch(RuntimeException ex){
                    throw new IllegalArgumentException(
                            "Invalid initialDelayString value \""+initialDelayString+"\" - cannot parse into long");
                }
            }
        }


        String cron=scheduled.cron();
        if(StringUtils.hasText(cron)){
            String zone=scheduled.zone();
            if(this.embeddedValueResolver!=null){
                cron=this.embeddedValueResolver.resolveStringValue(cron);
                zone=this.embeddedValueResolver.resolveStringValue(zone);
            }
            if(StringUtils.hasLength(cron)){
                Assert.isTrue(initialDelay==-1,"'initialDelay' not supported for cron triggers");
                processedSchedule=true;
                if(!Scheduled.CRON_DISABLED.equals(cron)){
                    TimeZone timeZone;
                    if(StringUtils.hasText(zone)){
                        timeZone=StringUtils.parseTimeZoneString(zone);
                    }
                    else{
                        timeZone=TimeZone.getDefault();
                    }
                    tasks.add(this.registrar.scheduleCronTask(new CronTask(runnable,new CronTrigger(cron,timeZone))));
                }
            }
        }


        if(initialDelay< 0){
            initialDelay=0;
        }


        long fixedDelay=convertToMillis(scheduled.fixedDelay(),scheduled.timeUnit());
        if(fixedDelay>=0){
            Assert.isTrue(!processedSchedule,errorMessage);
            processedSchedule=true;
            tasks.add(this.registrar.scheduleFixedDelayTask(new FixedDelayTask(runnable,fixedDelay,initialDelay)));
        }

        String fixedDelayString=scheduled.fixedDelayString();
        if(StringUtils.hasText(fixedDelayString)){
            if(this.embeddedValueResolver!=null){
                fixedDelayString=this.embeddedValueResolver.resolveStringValue(fixedDelayString);
            }
            if(StringUtils.hasLength(fixedDelayString)){
                Assert.isTrue(!processedSchedule,errorMessage);
                processedSchedule=true;
                try{
                    fixedDelay=convertToMillis(fixedDelayString,scheduled.timeUnit());
                }
                catch(RuntimeException ex){
                    throw new IllegalArgumentException(
                            "Invalid fixedDelayString value \""+fixedDelayString+"\" - cannot parse into long");
                }
                tasks.add(this.registrar.scheduleFixedDelayTask(new FixedDelayTask(runnable,fixedDelay,initialDelay)));
            }
        }


        long fixedRate=convertToMillis(scheduled.fixedRate(),scheduled.timeUnit());
        if(fixedRate>=0){
            Assert.isTrue(!processedSchedule,errorMessage);
            processedSchedule=true;
            tasks.add(this.registrar.scheduleFixedRateTask(new FixedRateTask(runnable,fixedRate,initialDelay)));
        }
        String fixedRateString=scheduled.fixedRateString();
        if(StringUtils.hasText(fixedRateString)){
            if(this.embeddedValueResolver!=null){
                fixedRateString=this.embeddedValueResolver.resolveStringValue(fixedRateString);
            }
            if(StringUtils.hasLength(fixedRateString)){
                Assert.isTrue(!processedSchedule,errorMessage);
                processedSchedule=true;
                try{
                    fixedRate=convertToMillis(fixedRateString,scheduled.timeUnit());
                }
                catch(RuntimeException ex){
                    throw new IllegalArgumentException(
                            "Invalid fixedRateString value \""+fixedRateString+"\" - cannot parse into long");
                }
                tasks.add(this.registrar.scheduleFixedRateTask(new FixedRateTask(runnable,fixedRate,initialDelay)));
            }
        }


        Assert.isTrue(processedSchedule,errorMessage);


        synchronized (this.scheduledTasks){
            Set<ScheduledTask> regTasks=this.scheduledTasks.computeIfAbsent(bean,key->new LinkedHashSet<>(4));
            regTasks.addAll(tasks);
        }
    }
    catch(IllegalArgumentException ex){
        throw new IllegalStateException(
                "Encountered invalid @Scheduled method '"+method.getName()+"': "+ex.getMessage());
    }
}
```

可以看到，processScheduled方法主要是使用embeddedValueResolver对带String后缀的属性进行从配置文件读取的操作，根据每个方法上使用的注解判断定时任务的类型是CronTask还是FixedRateTask，将这些任务添加到ScheduledTaskRegistrar中的unresolvedTasks

比较关键的代码
```java
tasks.add(this.registrar.scheduleCronTask(new CronTask(runnable, new CronTrigger(cron, timeZone))));
```


#### ScheduledTaskRegistrar 中解析 task

以 CronTask 为例，如果定义了 taskScheduler 则由 taskScheduler 执行，如果没有放到 unresolvedTasks 中。

```java
@Nullable
public ScheduledTask scheduleCronTask(CronTask task){
    ScheduledTask scheduledTask=this.unresolvedTasks.remove(task);
    boolean newTask=false;
    if(scheduledTask==null){
        scheduledTask=new ScheduledTask(task);
        newTask=true;
    }
    if(this.taskScheduler!=null){
        scheduledTask.future=this.taskScheduler.schedule(task.getRunnable(),task.getTrigger());
    }
    else{
        addCronTask(task);
        this.unresolvedTasks.put(task,scheduledTask);
    }
    return(newTask ? scheduledTask : null);
}
```

#### TaskScheduler 对 Task 处理

默认是 ConcurrentTaskScheduler， 处理方法如下

```java
@Override
@Nullable
public ScheduledFuture<?> schedule(Runnable task,Trigger trigger){
    try{
        if(this.enterpriseConcurrentScheduler){
            return new EnterpriseConcurrentTriggerScheduler().schedule(decorateTask(task,true),trigger);
        }
        else{
            ErrorHandler errorHandler=
                    (this.errorHandler!=null?this.errorHandler:TaskUtils.getDefaultErrorHandler(true));
            return new ReschedulingRunnable(task,trigger,this.clock,this.scheduledExecutor,errorHandler).schedule();
        }
    }
    catch(RejectedExecutionException ex){
        throw new TaskRejectedException("Executor ["+this.scheduledExecutor+"] did not accept task: "+task,ex);
    }
}
```

EnterpriseConcurrentTriggerScheduler 是 JSR-236 Trigger 标准，它的处理方法如下

```java
private class EnterpriseConcurrentTriggerScheduler {

    public ScheduledFuture<?> schedule(Runnable task, final Trigger trigger) {
        ManagedScheduledExecutorService executor = (ManagedScheduledExecutorService) scheduledExecutor;
        return executor.schedule(task, new javax.enterprise.concurrent.Trigger() {
            @Override
            @Nullable
            public Date getNextRunTime(@Nullable LastExecution le, Date taskScheduledTime) {
                return (trigger.nextExecutionTime(le != null ?
                        new SimpleTriggerContext(le.getScheduledStart(), le.getRunStart(), le.getRunEnd()) :
                        new SimpleTriggerContext()));
            }

            @Override
            public boolean skipRun(LastExecution lastExecution, Date scheduledRunTime) {
                return false;
            }
        });
    }
}

```

如果没有使用 EnterpriseConcurrentTriggerScheduler, 则使用 ReschedulingRunnable，本质上由 ScheduledExecutorService 处理

```java
public ReschedulingRunnable(Runnable delegate,Trigger trigger,Clock clock,
                            ScheduledExecutorService executor,ErrorHandler errorHandler){

    super(delegate,errorHandler);
    this.trigger=trigger;
    this.triggerContext=new SimpleTriggerContext(clock);
    this.executor=executor;
}


@Nullable
public ScheduledFuture<?> schedule(){
    synchronized (this.triggerContextMonitor){
        this.scheduledExecutionTime=this.trigger.nextExecutionTime(this.triggerContext);
        if(this.scheduledExecutionTime==null){
            return null;
        }
        long initialDelay=this.scheduledExecutionTime.getTime()-this.triggerContext.getClock().millis();
        this.currentFuture=this.executor.schedule(this,initialDelay,TimeUnit.MILLISECONDS);
        return this;
    }
}
```

在所有单例的Bean实例化完成后，调用afterSingletonsInstantiated() ，在Spring容器初始化完成后，触发ContextRefreshedEvent 事件，调用onApplicationEvent方法，执行finishRegistration()

```java
private void finishRegistration() {
    if (this.scheduler != null) {
        this.registrar.setScheduler(this.scheduler);
    }

    if (this.beanFactory instanceof ListableBeanFactory) {
        Map<String, SchedulingConfigurer> beans =
                ((ListableBeanFactory) this.beanFactory).getBeansOfType(SchedulingConfigurer.class);
        List<SchedulingConfigurer> configurers = new ArrayList<>(beans.values());
        AnnotationAwareOrderComparator.sort(configurers);
        for (SchedulingConfigurer configurer : configurers) {
            configurer.configureTasks(this.registrar);
        }
    }

    if (this.registrar.hasTasks() && this.registrar.getScheduler() == null) {
        Assert.state(this.beanFactory != null, "BeanFactory must be set to find scheduler by type");
        try {
            // Search for TaskScheduler bean...
            this.registrar.setTaskScheduler(resolveSchedulerBean(this.beanFactory, TaskScheduler.class, false));
        }
        catch (NoUniqueBeanDefinitionException ex) {
            logger.trace("Could not find unique TaskScheduler bean", ex);
            try {
                this.registrar.setTaskScheduler(resolveSchedulerBean(this.beanFactory, TaskScheduler.class, true));
            }
            catch (NoSuchBeanDefinitionException ex2) {
                if (logger.isInfoEnabled()) {
                    logger.info("More than one TaskScheduler bean exists within the context, and " +
                            "none is named 'taskScheduler'. Mark one of them as primary or name it 'taskScheduler' " +
                            "(possibly as an alias); or implement the SchedulingConfigurer interface and call " +
                            "ScheduledTaskRegistrar#setScheduler explicitly within the configureTasks() callback: " +
                            ex.getBeanNamesFound());
                }
            }
        }
        catch (NoSuchBeanDefinitionException ex) {
            logger.trace("Could not find default TaskScheduler bean", ex);
            // Search for ScheduledExecutorService bean next...
            try {
                this.registrar.setScheduler(resolveSchedulerBean(this.beanFactory, ScheduledExecutorService.class, false));
            }
            catch (NoUniqueBeanDefinitionException ex2) {
                logger.trace("Could not find unique ScheduledExecutorService bean", ex2);
                try {
                    this.registrar.setScheduler(resolveSchedulerBean(this.beanFactory, ScheduledExecutorService.class, true));
                }
                catch (NoSuchBeanDefinitionException ex3) {
                    if (logger.isInfoEnabled()) {
                        logger.info("More than one ScheduledExecutorService bean exists within the context, and " +
                                "none is named 'taskScheduler'. Mark one of them as primary or name it 'taskScheduler' " +
                                "(possibly as an alias); or implement the SchedulingConfigurer interface and call " +
                                "ScheduledTaskRegistrar#setScheduler explicitly within the configureTasks() callback: " +
                                ex2.getBeanNamesFound());
                    }
                }
            }
            catch (NoSuchBeanDefinitionException ex2) {
                logger.trace("Could not find default ScheduledExecutorService bean", ex2);
                // Giving up -> falling back to default scheduler within the registrar...
                logger.info("No TaskScheduler/ScheduledExecutorService bean found for scheduled processing");
            }
        }
    }

    this.registrar.afterPropertiesSet();
}
```

这个方法主要实现的内容是：

a. 用容器中的SchedulingConfigurer配置ScheduledTaskRegistrar,这里是根据ScheduledTaskRegistrar的引用，调用其set方法设置一些属性
public interface SchedulingConfigurer {
void configureTasks(ScheduledTaskRegistrar var1);
}
b. 如果此时ScheduledTaskRegistrar的scheduler还是空，就从容器中取TaskScheduler（byName和byType），如果没有取到就根据容器中的ScheduledExecutorService实例化TaskScheduler
c. this.registrar.afterPropertiesSet();
所以在容器中注入TaskScheduler或ScheduledExecutorService的类或者实现SchedulingConfigurer接口都可以配置定时任务的线程池



从afterPropertiesSet 又进入了熟悉的方法scheduleCronTask，在这里将任务提交给taskScheduler
```java
@SuppressWarnings("deprecation")
protected void scheduleTasks() {
    if (this.taskScheduler == null) {
        this.localExecutor = Executors.newSingleThreadScheduledExecutor();
        this.taskScheduler = new ConcurrentTaskScheduler(this.localExecutor);
    }
    if (this.triggerTasks != null) {
        for (TriggerTask task : this.triggerTasks) {
            addScheduledTask(scheduleTriggerTask(task));
        }
    }
    if (this.cronTasks != null) {
        for (CronTask task : this.cronTasks) {
            addScheduledTask(scheduleCronTask(task));
        }
    }
    if (this.fixedRateTasks != null) {
        for (IntervalTask task : this.fixedRateTasks) {
            addScheduledTask(scheduleFixedRateTask(task));
        }
    }
    if (this.fixedDelayTasks != null) {
        for (IntervalTask task : this.fixedDelayTasks) {
            addScheduledTask(scheduleFixedDelayTask(task));
        }
    }
}
```