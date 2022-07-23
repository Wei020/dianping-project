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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;


    @GetMapping("/queryRecords")
    public Result queryRecords(){
        List<Message> result = chatService.list();
        log.info("-----------------{}", result.toString());
        return Result.ok(result);
    }
}
