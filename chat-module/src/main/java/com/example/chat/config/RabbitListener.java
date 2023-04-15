package com.example.chat.config;

import com.alibaba.fastjson.JSONObject;
import com.example.chat.dto.NoticeDTO;
import com.example.chat.service.NoticeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitListener {

    @Autowired
    private NoticeService noticeService;

    @org.springframework.amqp.rabbit.annotation.RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "notice.queue"),
            exchange = @Exchange(name = "notice.direct"),
            key = "notice"
    ))
    public void listerFollowQueue(NoticeDTO noticeDTO) {
        log.info("取出消息:" + JSONObject.toJSONString(noticeDTO.toString()));
        noticeService.notice(noticeDTO);
    }

}
