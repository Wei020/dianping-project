package com.example.user.controller;



import com.example.user.entity.Message;
import com.example.user.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class ChatController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ChatService chatService;

    @MessageMapping("/hello")
    public void greeting(Message message){
        log.info(message.getFrom() + "-------" + message.getContent());

        simpMessagingTemplate.convertAndSend("/topic/greetings",message);
    }
}
