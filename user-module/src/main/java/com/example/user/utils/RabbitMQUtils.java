package com.example.user.utils;

import com.example.user.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RabbitMQUtils {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void SendMessageInfoQueue(UserDTO userDTO) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
//        异步回调
        correlationData.getFuture().addCallback(confirm -> {
            if(confirm.isAck()){
                // 3.1.ack，消息成功
                log.debug("消息成功发送到交换机, ID:{}", correlationData.getId());
            }else{
                // 3.2.nack，消息失败
                log.error("消息发送到交换机失败, ID:{}, 原因{}",correlationData.getId(), confirm.getReason());
//                重发消息
                rabbitTemplate.convertAndSend("info.direct", "info", userDTO, correlationData);
            }
        }, throwable ->{
            log.error("消息发送异常, ID:{}, 原因{}",correlationData.getId(),throwable.getMessage());
            rabbitTemplate.convertAndSend("info.direct", "info", userDTO, correlationData);
        }) ;
        rabbitTemplate.convertAndSend("info.direct", "info", userDTO, correlationData);
    }


}
