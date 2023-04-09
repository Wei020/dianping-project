package com.example.blog;

import com.example.feign.clients.ChatClient;
import com.example.feign.clients.UserClient;
import com.example.feign.config.DefaultFeignConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(clients = {UserClient.class, ChatClient.class}, defaultConfiguration = DefaultFeignConfiguration.class)
public class BlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }
}
