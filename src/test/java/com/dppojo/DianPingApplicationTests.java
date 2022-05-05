package com.dppojo;

import com.dppojo.service.SendMailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DianPingApplicationTests {

    @Autowired
    private SendMailService sendMailService;

    @Test
    void testIdWorker(){
        sendMailService.sendMail("780559756@qq.com", "555666");
    }
}
