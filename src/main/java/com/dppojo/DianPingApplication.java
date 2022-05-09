package com.dppojo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@MapperScan("com.dppojo.mapper")
@SpringBootApplication
public class DianPingApplication {
    public static void main(String[] args) {
        SpringApplication.run(DianPingApplication.class, args);
    }
}
