package com.example.gateway;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//@Order(-1)//值越低优先级越高
@Component
@Slf4j
public class AuthorizeFilter implements GlobalFilter, Ordered {

    private static final String LOGIN_USER_KEY = "login:token:";
    private static final long LOGIN_USER_TTL = 20L;
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.获取请求参数
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String uri = exchange.getRequest().getURI().getPath();
        String[] urls = new String[]{
                "/shop/**",
                "/shop-type/**",
                "/voucher/**",
                "/user/phoned",
                "/user/emailed",
                "/user/login",
                "/user/findPassword",
                "/blog/hot"
        };
        boolean check = check(urls, uri);
        // 2.获取authorization参数
        String token = headers.getFirst("authorization");
        String key = LOGIN_USER_KEY + token;
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash()
                .entries(key);
        // 3.校验
        if (!userMap.isEmpty() || check) {
            // 放行,刷新token有效期
            if(!userMap.isEmpty())
                stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);
            return chain.filter(exchange);
        }
        // 4.拦截
        // 4.1.禁止访问，设置状态码
        if(exchange.getResponse().getStatusCode().value() != 401){
//            防止连续弹窗2次
            exchange.getResponse().setStatusCode(HttpStatus.resolve(401));
        }
        // 4.2.结束处理
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public boolean check(String[] urls, String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
