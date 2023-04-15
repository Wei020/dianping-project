package com.example.blog.utils;

import com.example.blog.dto.FollowDTO;
import com.example.feign.dto.NoticeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RabbitMQUtils {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void SendMessageFollowQueue(FollowDTO followDTO){
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
                rabbitTemplate.convertAndSend("follow.direct", "follow", followDTO,correlationData);
            }
        }, throwable ->{
            log.error("消息发送异常, ID:{}, 原因{}",correlationData.getId(),throwable.getMessage());
            rabbitTemplate.convertAndSend("follow.direct", "follow", followDTO,correlationData);
        }) ;
        rabbitTemplate.convertAndSend("follow.direct","follow", followDTO,correlationData);
    }

    public void SendMessageNoticeQueue(NoticeDTO noticeDTO) {
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
                rabbitTemplate.convertAndSend("notice.direct", "notice", noticeDTO, correlationData);
            }
        }, throwable ->{
            log.error("消息发送异常, ID:{}, 原因{}",correlationData.getId(),throwable.getMessage());
            rabbitTemplate.convertAndSend("notice.direct", "notice", noticeDTO, correlationData);
        }) ;
        rabbitTemplate.convertAndSend("notice.direct","notice", noticeDTO, correlationData);
    }


}
