package com.example.user.controller;



import com.example.user.dto.Result;
import com.example.user.entity.Message;
import com.example.user.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
public class WsController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ChatService chatService;

    @MessageMapping("/hello")
    public void greeting(Message message){
        log.info(message.getFromNickname() + "-------" + message.getContent());
        chatService.saveEntity(message);
        simpMessagingTemplate.convertAndSend("/topic/greetings",message);
    }
}
