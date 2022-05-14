package com.example.shop.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class CommonConfig implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
//        路由失败回调
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback(){
            @Override
            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
                log.info("消息发送到队列失败，应答码{}，原因{}，交换机{}，路由键{},消息{}",
                        i, s, s1, s2, message.toString());
//                可以做消息重发
                rabbitTemplate.convertAndSend("order.direct", "order.add", message);
            }
        });
    }

}
