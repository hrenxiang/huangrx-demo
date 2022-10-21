package com.huangrx.concurrent.synchroniz;

import java.util.concurrent.TimeUnit;

/**
 * 多线程的8个问题：[synchronized 锁的是当前对象本身]
 * <p>
 * 1. 标准访问，先打印短信还是邮件 [sms email]
 * 2. 停4秒在短信方法内，先打印短信还是邮件 [sms email]
 * 3. 停4秒在短信方法内，新增普通的hello方法，是先打短信还是hello [hello 4秒后sms]
 * 4. 停4秒在短信方法内，现在有两部手机，先打印短信还是邮件 [email 四秒后sms]
 * 5. 两个静态同步方法，1部手机，先打印短信还是邮件 [sms email 静态同步方法锁的是当前class对象]
 * 6. 两个静态同步方法，2部手机，先打印短信还是邮件 [sms email 静态同步方法锁的是当前class对象]
 * 7. 1个静态同步方法，1个普通同步方法，1部手机，先打印短信还是邮件 [email sms 锁的class和当前实例对象是不一样的]
 * 8. 1个静态同步方法，1个普通同步方法，2部手机，先打印短信还是邮件 [email sms]
 *
 * @author hrenxiang
 * @since 2022-10-20 18:19:02
 */
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

        //Thread.sleep(10);

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

class Phone {

    public static synchronized void sendSMS() throws Exception {
        TimeUnit.SECONDS.sleep(4);
        System.out.println("------sendSMS");
    }

    public static synchronized void sendEmail() throws Exception {
        System.out.println("------sendEmail");
    }

    public void getHello() {
        System.out.println("------getHello");
    }

}