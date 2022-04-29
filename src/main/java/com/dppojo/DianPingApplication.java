package com.dppojo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.hmdp.mapper")
@SpringBootApplication
public class DianPingApplication {
    public static void main(String[] args) {
        SpringApplication.run(DianPingApplication.class, args);
    }
}
