package com.huangrx.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hrenxiang
 * @since 2022-07-05 14:01
 */
@Slf4j
public class Car {

    @Autowired
    private UserService userService;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void update(String name) {
        userService.updateEmpNameById(7369, name);
        log.info("32234");
    }
}
