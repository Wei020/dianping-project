package com.example.chat.controller;



import com.example.chat.entity.Message;
import com.example.chat.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@Slf4j
public class WsController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/hello")
    public void greeting(Message message){
        log.info("传入信息为:{}", message.toString());
        if(message.getFromId() == null)
            message.setFromId(0l);
        message.setSendTime(LocalDateTime.now());
        if(message.getToId() == null)
            message.setToId(0l);
        messageService.save(message);
        simpMessagingTemplate.convertAndSend("/topic/greetings",message);
    }

    @MessageMapping("/chat")
    public void chatting(Message message){
        log.info("信息为:" + message);
        if(message.getFromId() == null)
            message.setFromId(0l);
        message.setSendTime(LocalDateTime.now());
        if(message.getToId() == null)
            message.setToId(0l);
        messageService.save(message);
        simpMessagingTemplate.convertAndSendToUser(message.getToId().toString(), "/chat", message);
    }
}
