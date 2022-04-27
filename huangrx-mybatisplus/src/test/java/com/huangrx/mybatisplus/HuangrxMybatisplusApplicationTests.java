package com.huangrx.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.huangrx.mybatisplus.generator.CodeGenerator;
import com.huangrx.mybatisplus.model.entity.Dept;
import com.huangrx.mybatisplus.model.entity.User;
import com.huangrx.mybatisplus.service.DeptService;
import com.huangrx.mybatisplus.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class HuangrxMybatisplusApplicationTests {

    @Autowired
    private CodeGenerator codeGenerator;

    @Autowired
    private DeptService deptService;

    @Autowired
    private UserServiceImpl userService;

    @BeforeEach
    void testBefore(){
        log.info("测试开始!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    @AfterEach
    void testAfter(){
        log.info("测试结束!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    @Test
    void contextLoads() {
        codeGenerator.startGenerator();
    }

    @Test
    void testUser() {
        User one = userService.getOne(
                Wrappers.<User>lambdaQuery()
                        .eq(User::getId, 1)
        );
        log.info("结果: {}", one);

        Dept one1 = deptService.getOne(
                Wrappers.<Dept>lambdaQuery()
                        .eq(Dept::getId, 1)
        );
        log.info("结果: {}", one1);
    }
}
