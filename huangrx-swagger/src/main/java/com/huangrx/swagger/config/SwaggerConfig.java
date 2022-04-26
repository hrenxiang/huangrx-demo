package com.huangrx.swagger.config;

import com.huangrx.swagger.state.ResponseStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.*;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * swagger 配置类
 *
 * @author hrenxiang
 * @since 2022-04-25 4:52 PM
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Bean
    public Docket openApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("huangrx demo api 1.0")
                .apiInfo(apiinfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.huangrx.swagger.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalRequestParameters(getGlobalRequestParameters())
                .globalResponses(HttpMethod.GET, getGlobalResponse());
    }

    private List<Response> getGlobalResponse() {
        return ResponseStatus.HTTP_STATUS_ALL.stream().map(
                        a -> new ResponseBuilder().code(a.getResponseCode()).description(a.getDescription()).build())
                .collect(Collectors.toList());
    }


    private List<RequestParameter> getGlobalRequestParameters() {
        List<RequestParameter> parameters = new ArrayList<>();
        parameters.add(new RequestParameterBuilder()
                .name("AppKey")
                .description("App Key")
                .required(false)
                .in(ParameterType.QUERY)
                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
                .required(false)
                .build());
        return parameters;
    }

    private ApiInfo apiinfo() {
        return new ApiInfoBuilder()
                .title("Swagger API")
                .description("test api")
                .contact(new Contact("huangrx", "https://hrenxiang.github.io/blog/", "h2295701930@gmail.com"))
                .termsOfServiceUrl("https://www.baidu.com")
                .version("1.0")
                .build();
    }
}
