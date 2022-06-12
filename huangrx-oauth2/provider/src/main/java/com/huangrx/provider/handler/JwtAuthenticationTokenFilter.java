//package com.huangrx.provider.handler;
//
//import com.huangrx.provider.util.JwtTokenUtil;
//import lombok.NonNull;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.annotation.Resource;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * JWT登录授权过滤器
// *
// * @author hrenxiang
// * @since 2022-04-24 9:48 PM
// */
//public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
//    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);
//
//    @Resource
//    private UserDetailsService userDetailsService;
//    @Resource
//    private JwtTokenUtil jwtTokenUtil;
//
//    @Value("${jwt.tokenHeader}")
//    private String tokenHeader;
//    @Value("${jwt.tokenHead}")
//    private String tokenHead;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    @NonNull HttpServletResponse response,
//                                    @NonNull FilterChain chain) throws ServletException, IOException {
//        // 从客户端请求中获取 JWT
//        String authHeader = request.getHeader(this.tokenHeader);
//        // 该 JWT 是我们规定的格式，以 tokenHead 开头
//        if (authHeader != null && authHeader.startsWith(this.tokenHead)) {
//            // The part after "Bearer "
//            String authToken = authHeader.substring(this.tokenHead.length());
//            // 从 JWT 中获取用户名
//            String username = jwtTokenUtil.getUserNameFromToken(authToken);
//            LOGGER.info("checking username:{}", username);
//
//            // SecurityContextHolder 是 SpringSecurity 的一个工具类
//            // 保存应用程序中当前使用人的安全上下文
//            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                // 根据用户名获取登录用户信息
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                // 验证 token 是否过期
//                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
//                    // 将登录用户保存到安全上下文中
//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
//                            null, userDetails.getAuthorities());
//                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//
//                    LOGGER.info("authenticated user:{}", username);
//                }
//            }
//        }
//        chain.doFilter(request, response);
//    }
//}