package com.huangrx.i18n.controller;

import com.alibaba.fastjson.JSON;
import com.huangrx.i18n.domain.Response;
import com.huangrx.i18n.inteceptor.Container;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * @author        hrenxiang
 * @since         2022-08-17 11-22-22
 */
@RestController
@RequestMapping("/i18n")
@Slf4j
public class I18nController {

    @Resource
    private MessageSource messageSource;

    /**
     * 默认访问路径后 加参数 lang=en_US 即可变换语言
     *
     * @return 返回值
     */
    @GetMapping("/hello")
    public String hello(){
        Locale locale = LocaleContextHolder.getLocale();
        log.info("LocaleContextHolder.getLocale(): country - {}, lang - {}, default - {}",
                locale.getCountry(), locale.getLanguage(), Locale.getDefault());
        return messageSource.getMessage("user.name", null, locale);
    }

    /**
     * 不使用默认的，指定传参修改语言
     * @param language 语言
     * @return 返回值
     */
    @GetMapping(path = "response")
    @ResponseBody
    public String paramLang(String language) {
        String[] s = language.split("_");
        LocaleContextHolder.setLocale(new Locale(s[0], s[1]));
        Response res = new Response<>().setCode(200).setMsg(messageSource.getMessage("200", null, LocaleContextHolder.getLocale())).setData(true);
        return JSON.toJSONString(res);
    }

    /**
     * 拦截header 中配置的语言，进行相应变换
     * @return 返回值
     */
    @GetMapping(path = "header")
    @ResponseBody
    public String headerLang() {
        log.info("Container.PageLanguage.get(): {}", Container.PageLanguage.get());
        Response res = new Response<>().setCode(200).setMsg(messageSource.getMessage("200", null, LocaleContextHolder.getLocale())).setData(true);
        return JSON.toJSONString(res);
    }

}