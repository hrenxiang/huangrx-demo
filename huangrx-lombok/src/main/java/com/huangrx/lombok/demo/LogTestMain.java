package com.huangrx.lombok.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Date;

/**
 * ' @Log '：根据不同的注解生成不同类型的log对象，但是实例名称都是log，有六种可选实现类
 * ' @CommonsLog ' Creates log = org.apache.commons.logging.LogFactory.getLog(LogExample.class);
 * ' @Log ' Creates log = java.util.logging.Logger.getLogger(LogExample.class.getName());
 * ' @Log4j ' Creates log = org.apache.log4j.Logger.getLogger(LogExample.class);
 * ' @Log4j2 ' Creates log = org.apache.logging.log4j.LogManager.getLogger(LogExample.class);
 * ' @Slf4j ' Creates log = org.slf4j.LoggerFactory.getLogger(LogExample.class);
 * ' @XSlf4j ' Creates log = org.slf4j.ext.XLoggerFactory.getXLogger(LogExample.class);
 *
 * 注意，需要引入对应的包
 *
 * @author hrenxiang
 * @since 2022-04-25 2:21 PM
 */
@Slf4j
public class LogTestMain {

    public static void main(String[] args) {
        log.trace("test");
        log.debug("test");
        log.info("test");
        log.warn("test");
        log.error("test");

        Date date = null;
        try {

            System.out.println(date.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            log.info("==========================================");
            log.error("error : {} ", e.getMessage());
            log.error("error : {} ", e);
            log.error("error : {},{} ", 1, e);
            log.error("error: {}", ExceptionUtils.getStackTrace(e));
        }
    }
}
