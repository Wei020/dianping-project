package com.example.user.controller;



import com.example.user.dto.Result;
import com.example.user.dto.UserDTO;
import com.example.user.entity.Message;
import com.example.user.entity.User;
import com.example.user.service.ChatService;
import com.example.user.service.UserService;
import com.example.user.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
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
        log.info("传入信息为:{}", message.toString());
        if(message.getFromId() == null)
            message.setFromId(0l);
        if(message.getFromNickname() == null)
            message.setFromNickname("");
        message.setSendTime(LocalDateTime.now());
        if(message.getToId() == null)
            message.setToId(0l);
        chatService.save(message);
        simpMessagingTemplate.convertAndSend("/topic/greetings",message);
    }
}
