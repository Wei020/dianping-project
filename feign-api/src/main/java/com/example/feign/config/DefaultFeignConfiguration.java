package com.example.feign.config;

import com.example.feign.fallback.ShopClientFallbackFactory;
import feign.Logger;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfiguration {

    @Bean
    public Logger.Level logLevel(){
        return Logger.Level.BASIC;
    }

    @Bean
    public ShopClientFallbackFactory shopClientFallbackFactory(){
        return new ShopClientFallbackFactory();
    }
}
