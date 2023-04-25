package com.example.feign.config;

import com.example.feign.fallback.ChatClientFallbackFactory;
import com.example.feign.fallback.UserClientFallbackFactory;
import feign.Logger;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfiguration {

    @Bean
    public Logger.Level logLevel(){
        return Logger.Level.BASIC;
    }

    @Bean
    public UserClientFallbackFactory userClientFallbackFactory(){
        return new UserClientFallbackFactory();
    }

    @Bean
    public ChatClientFallbackFactory chatClientFallbackFactory(){
        return new ChatClientFallbackFactory();
    }
}
