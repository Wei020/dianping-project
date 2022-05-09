package com.dppojo;

import com.dppojo.entity.VoucherOrder;
import com.dppojo.service.IVoucherOrderService;
import com.dppojo.service.SendMailService;
import com.dppojo.service.impl.VoucherOrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DianPingApplicationTests {

    @Autowired
    private SendMailService sendMailService;

    @Autowired
    private VoucherOrderServiceImpl voucherOrderService;

    @Test
    void testIdWorker(){
//        sendMailService.sendMail("422953951@qq.com", "555666");
    }

    @Test
    void testError() throws InterruptedException {
//        VoucherOrder voucherOrder = new VoucherOrder();
//        voucherOrderService.SendMessageOrderQueue(voucherOrder);
    }
}
