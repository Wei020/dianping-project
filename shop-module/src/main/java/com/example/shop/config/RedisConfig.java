package com.example.shop.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

//    配置redis分布式锁
    @Bean
    public RedissonClient redissonClient(){
        Config config =new Config();
        config.useSingleServer().setAddress("redis://192.168.10.100:6379")
                .setPassword("123321");
        return Redisson.create(config);
    }
}
