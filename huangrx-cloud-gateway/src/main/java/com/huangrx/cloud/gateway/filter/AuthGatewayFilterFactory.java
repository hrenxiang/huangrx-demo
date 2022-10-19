//package com.huangrx.cloud.gateway.filter;
//
//import com.atguigu.gmall.common.utils.IpUtil;
//import com.atguigu.gmall.common.utils.JwtUtils;
//import com.atguigu.gmall.gateway.config.JwtProperties;
//import lombok.Data;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.http.HttpCookie;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author hrenxiang
// * @create 2021/10/22 7:09 下午
// */
//@EnableConfigurationProperties(JwtProperties.class)
//@Component
//public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthGatewayFilterFactory.PathConfig> {
//
//    @Autowired
//    private JwtProperties jwtProperties;
//
//    /**
//     * 配置类中的字段类型
//     *
//     * @return
//     */
//    @Override
//    public ShortcutType shortcutType() {
//        return ShortcutType.GATHER_LIST;
//    }
//
//    /**
//     * 配置类中的字段顺序
//     *
//     * @return
//     */
//    @Override
//    public List<String> shortcutFieldOrder() {
//        return Arrays.asList("paths");
//    }
//
//    /**
//     * 配置类，配置文件中配置的要过滤的路径集合
//     */
//    @Data
//    public static class PathConfig {
//        private List<String> paths;
//    }
//
//    /**
//     * 一定要重写构造方法
//     * 告诉父类，这里使用PathConfig对象接收配置内容
//     */
//    public AuthGatewayFilterFactory() {
//        super(PathConfig.class);
//    }
//
//    @Override
//    public GatewayFilter apply(PathConfig config) {
//        return (exchange, chain) -> {
//
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//            List<String> paths = config.getPaths();
//            String requestPath = request.getURI().getPath();
//
//            //1. 判断请求路径在不在拦截名单中，不在直接放行
//            if (!CollectionUtils.isEmpty(paths) && paths.stream().noneMatch(path -> path.startsWith(requestPath))){
//                return chain.filter(exchange);
//            }
//
//            //2. 获取请求中的token。同步请求从cookie中获取，异步请求从header中获取
//            //走cookie太重，一个网站往往有很多cookie，如果通过携带cookie的方式传递token，网络传输压力太大
//            HttpHeaders headers = request.getHeaders();
//            String token = request.getHeaders().getFirst("token");
//            //如果token为空直接从cookie中获取
//            if (StringUtils.isEmpty(token)){
//                MultiValueMap<String, HttpCookie> cookies = request.getCookies();
//                if (!CollectionUtils.isEmpty(cookies)&&cookies.containsKey(this.jwtProperties.getCookieName())){
//                    token = cookies.getFirst(this.jwtProperties.getCookieName()).getValue();
//                }
//            }
//
//            //3. 判断token是否为空。为空直接拦截
//            if(StringUtils.isEmpty(token)){
//                // 303状态码表示由于请求对应的资源存在着另一个URI，应使用重定向获取请求的资源
//                response.setStatusCode(HttpStatus.SEE_OTHER);
//                response.getHeaders().set(HttpHeaders.LOCATION,"http://sso.gmall.com/toLogin.html?returnUrl="+request.getURI());
//                return response.setComplete();
//            }
//
//
//            try {
//                //4. 如果不为空，解析jwt获取登录信息。解析异常直接拦截
//                Map<String, Object> map = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
//
//                //5. 判断是否被盗用。通过登录信息中的ip和当前请求的ip比较
//                String ip = map.get("ip").toString();
//                if (!StringUtils.equals(ip, IpUtil.getIpAddressAtGateway(request))){
//                    response.setStatusCode(HttpStatus.SEE_OTHER);
//                    response.getHeaders().set(HttpHeaders.LOCATION,"http://sso.gmall.com/toLogin.html?returnUrl="+request.getURI());
//                    return response.setComplete();
//                }
//
//                //6. 传递登录信息给后续服务。后续各服务就不用再去解析了,因为解析是很慢的，所以能不去解析就不去
//                request.mutate().header("userId",map.get("userId").toString()).header("userName",map.get("userName").toString()).build();
//                // 将新的request对象转换为 exchage对象
//                exchange.mutate().request(request).build();
//
//                //7. 放行
//                return chain.filter(exchange);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                // 如果失败，也去从新定向到登录页面
//                response.setStatusCode(HttpStatus.SEE_OTHER);
//                response.getHeaders().set(HttpHeaders.LOCATION,"http://sso.gmall.com/toLogin.html?returnUrl="+request.getURI());
//                return response.setComplete();
//            }
//
//        };
//    }
//}
