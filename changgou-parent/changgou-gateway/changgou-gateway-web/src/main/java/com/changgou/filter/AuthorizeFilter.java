package com.changgou.filter;

import com.changgou.utils.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-19 20:31
 * @description 鉴权过滤器
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    //令牌头名字
    private static final String AUTHORIZE_TOKEN = "Authorization";
    //登陆页面
    private static final String LOGIN_URL="http://localhost:9001/oauth/login";


    /**
    * @Description: 全局过滤器
    * @Param: [exchange, chain]
    * @Return: reactor.core.publisher.Mono<java.lang.Void>
    * @Author: Wangqibo
    * @Date: 2020/8/19/0019
    */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取request,response
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //获取请求的URI
        String path = request.getURI().getPath();
        //如果是登录就放行
        if (path.startsWith("/api/user/login")) {
            Mono<Void> filter = chain.filter(exchange);
            return filter;
        }
        //获取头文件中的令牌信息
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
        //如果头文件没有就从请求参数中获取
        if (StringUtils.isEmpty(token)) {
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        }
        //如果参数中没有，就从Cookie中获取
        if (StringUtils.isEmpty(token)) {
            HttpCookie cookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
            if (cookie != null) {
                token = cookie.getValue();
            }
        }
        //如果为空拒绝访问
        if (StringUtils.isEmpty(token)) {
            response.getHeaders().set("Location",LOGIN_URL+"?urlBack="+request.getURI().toString());
            response.setStatusCode(HttpStatus.SEE_OTHER);
            return response.setComplete();
        }
        //如果有token，解析，成功放行，失败拒绝
        try {
            //JwtUtil.parseJWT(token);
            //将令牌数据添加到头文件中
            request.mutate().header(AUTHORIZE_TOKEN, "Bearer " + token);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
