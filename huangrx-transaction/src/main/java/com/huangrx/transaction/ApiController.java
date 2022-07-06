package com.huangrx.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 控制器
 *
 * @author hrenxiang
 * @since 2022-07-04 17:34
 */
@RestController
@Slf4j
public class ApiController {

    @Resource
    UserService userService;

    @Resource
    ApiService apiService;

    @Resource
    JdbcTemplate jdbcTemplate;

    @GetMapping("get")
    public String test(@RequestParam("name") String name) {
        apiService.update(name);
        log.info("不同类别方法调用 - 外层控制");
        return "123";
    }

    /**
     * 同类中方法调用，事务注解失效
     * @param salary
     * @return
     */
    @GetMapping("get2")
    public String test2(@RequestParam("salary") Double salary) {
       this.update3(salary);
       log.info("同类方法调用 - 外层控制");
       return "123";
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void update3(Double salary) {
        userService.updateEmpSalaryById(7369, salary);
        log.info("同类方法调用 - 内部方法控制");
    }

}
