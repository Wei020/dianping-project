package com.dppojo;

import com.dppojo.utils.RedisIdWorker;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class DianPingApplicationTests {

    @Resource
    private RedisIdWorker redisIdWorker;

//    private ExecutorService es = Executors.newFixedThreadPool(500);

    @Test
    void testIdWorker(){
        System.out.println(redisIdWorker.nextId("order"));;
    }
}
