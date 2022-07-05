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

    @GetMapping("get")
    @Transactional(propagation = Propagation.NEVER, rollbackFor = Exception.class)
    public void test() {
        this.update();
        log.info("lllll");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void update() {
        userService.updateEmpNameById(7369, "ggggg");
        log.info("32234");
    }

    private void update2() {
        userService.updateEmpNameById(7369, "rrrrrrr");
        System.out.println(12);
    }

}
