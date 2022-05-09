package com.dppojo.listener;

import com.dppojo.service.SendMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@Slf4j
public class RabbitMqListener {

    @Autowired
    private SendMailService sendMailService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "code.queue"),
            exchange = @Exchange(name = "code.direct", type = ExchangeTypes.DIRECT),
            key = "code.email"
    ))
    public void listenCodeQueue(Map<String, String> map){
        String to = map.get("email");
        String code = map.get("code");
        log.info("验证码:" + code);
        sendMailService.sendMail(to, code);
    }

}
