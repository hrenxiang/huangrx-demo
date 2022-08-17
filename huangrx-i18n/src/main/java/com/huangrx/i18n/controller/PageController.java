package com.huangrx.i18n.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 跳转页面
 *
 * @author hrenxiang
 * @since 2022-08-17 14:44
 */
@Controller
public class PageController {

    /**
     * 使用@ResponseData此注解之后不会再走视图处理器，而是直接将数据写入到输入流中，他的效果等同于通过response对象输出指定格式的数据。
     * @param model 模块
     * @return 视图
     */
    @RequestMapping("hello")
    public String hello(Model model){
        // 创建一个model，添加变量msg
        model.addAttribute("msg", "hello springboot");
        return "hello";
    }

    /**
     * thymeleaf  #{…} 消息表达式可用于国际化文字信息，url后加 lang=en_US可以切换配置的语言
     * @return
     */
    @RequestMapping("index")
    public String hello(){
        return "index";
    }
}
