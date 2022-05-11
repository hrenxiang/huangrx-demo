package com.huangrx.mvc.config;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.huangrx.mvc.interceptor.PageLanguageHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * MVC 配置类
 *
 * @author hrenxiang
 * @since 2022-05-11 12:03 PM
 */
@Configuration
public class MyInterceptorConfig extends WebMvcConfigurationSupport {

    /**
     * 静态资源配置
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

    /**
     * 消息转换器配置
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));

        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));

        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)));

        objectMapper.registerModule(javaTimeModule);
        // 日期格式化
        converter.setObjectMapper(objectMapper);

        // 消息转换器 处理中文乱码问题
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.ALL);
        converter.setSupportedMediaTypes(supportedMediaTypes);

        converters.add(0, converter);
        super.extendMessageConverters(converters);
    }

    /**
     * 跨域配置
     */
    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        // 配置允许跨域的路径
        registry.addMapping("/**")
                // 配置允许访问的跨域资源的请求域名
                .allowedOrigins("*")
                // 配置允许请求header的访问，如 ：X-TOKEN
                .allowedHeaders("*")
                // 配置允许访问该跨域资源服务器的请求方法，如：POST、GET、PUT、DELETE等
                .allowedMethods("*")
                .maxAge(3600)
                .allowCredentials(true);
        super.addCorsMappings(registry);
    }

    /**
     * 自定义拦截器
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器，添加拦截路径和排除拦截路径
        List<String> pageLanguageUrlList = new ArrayList<>();
        pageLanguageUrlList.add("/rosettaMvpDictionary/**");
        pageLanguageUrlList.add("/rosettaMvpInformationManagement/queryMvpInformation");
        pageLanguageUrlList.add("/rosettaMvpInformationManagement/export");
        registry.addInterceptor(new PageLanguageHandler())
                .addPathPatterns(pageLanguageUrlList)
                .excludePathPatterns(new ArrayList<>());
        super.addInterceptors(registry);
    }

    /**
     * 视图控制器配置
     *
     * 下面代码等同于
     */
    //public class controller {
    //   @RequestMapping(value = { "/", "/index" })
    //   public String index() {
    //        return "index";
    //   }
    //   @RequestMapping(value = "article")
    //   public String article() {
    //        return "article";
    //   }
    //   @RequestMapping(value = "/error/404")
    //   public String error_404() {
    //        return "/error/404";
    //   }
    //  @RequestMapping(value = "/error/500")
    //   public String error_500() {
    //        return "/error/500";
    //   }
    //}
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //默认视图跳转
        registry.addViewController("/").setViewName("/index");
        registry.addViewController("/index").setViewName("/index");
        registry.addViewController("/article").setViewName("/article");
        registry.addViewController("/error/404").setViewName("/error/404");
        registry.addViewController("/error/500").setViewName("/error/500");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }

    /**
     * 视图解析器
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("");
        viewResolver.setSuffix(".html");
        viewResolver.setCache(false);
        viewResolver.setContentType("text/html;charset=UTF-8");
        viewResolver.setOrder(0);
        registry.viewResolver(viewResolver);
        super.configureViewResolvers(registry);
    }
}
