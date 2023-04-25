package com.example.blog.config;


import com.netflix.loadbalancer.BestAvailableRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfigConfiguration {

    @Bean
    public IRule loadBalancedRule() {
        return new BestAvailableRule();
    }
}
