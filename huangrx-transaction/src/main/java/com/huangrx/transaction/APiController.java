package com.huangrx.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

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

    @Autowired
    Car car;

    @GetMapping("get")
    public String test(@RequestParam("name") String name) {
        car.update(name);
        //this.update3();
        log.info("lllll");
        return "hhhh";
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void update3() {
        userService.updateEmpSalaryById(7369, 200d);
        log.info("32234");
    }

    public static void main(String[] args) {
        StackTraceElement[] stackTrace1 = Thread.currentThread().getStackTrace();
        StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace1) {
            System.out.println(stackTraceElement.getMethodName());
        }
        System.out.println("=======");
        for (StackTraceElement stackTraceElement1 : stackTrace) {
            System.out.println(stackTraceElement1.getMethodName());
        }
    }

}
