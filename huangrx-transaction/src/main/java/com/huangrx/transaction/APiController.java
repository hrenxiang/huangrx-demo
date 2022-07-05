package com.huangrx.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 控制器
 *
 * @author hrenxiang
 * @since 2022-07-04 17:34
 */
@Controller
@Slf4j
public class APiController {

    @Autowired
    UserService userService;

    /**
     * 如果外层方法开启事务，且内层方法也开启事务
     *
     * 隔离级别为，外层：REQUIRES_NEW    内层：REQUIRES_NEW     内层方法完成后，数据库数据并未修改
     * 隔离级别为，外层：REQUIRES_NEW    内层：REQUIRED         内层方法完成后，数据库数据并未修改
     * 隔离级别为，外层：REQUIRED        内层：REQUIRED         内层方法完成后，数据库数据并未修改
     * 隔离级别为，外层：REQUIRED        内层：REQUIRES_NEW     内层方法完成后，数据库数据并未修改
     * 隔离级别为，外层：NEVER           内层：REQUIRED         内层方法完成后，数据库数据修改
     *
     * 也就是说，如果想让内层方法执行完就提交，那么外层方法不建议使用事务
     */
    @GetMapping("get")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String test() {
        this.update();
        //this.update3();
        log.info("lllll");
        return "hhhh";
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void update() {
        userService.updateEmpNameById(7369, "sdfasfadsf");
        log.info("32234");
        System.out.println(123);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void update3() {
        userService.updateEmpSalaryById(7369, 200d);
        log.info("32234");
    }

    private void update2() {
        userService.updateEmpNameById(7369, "rrrrrrr");
        System.out.println(12);
    }

}
