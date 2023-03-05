package com.example.user.controller;



import com.example.user.dto.Result;
import com.example.user.entity.Chat;
import com.example.user.entity.Message;
import com.example.user.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;


    @GetMapping("/queryRecords")
    public Result queryRecords(@RequestParam("chatId") Long chatId){
        List<Message> result = chatService.queryByChatId(chatId);
        log.info("-----------------{}", result.toString());
        return Result.ok(result);
    }

    @GetMapping("/myChats")
    public Result queryChats(@RequestParam("id") Long id){
        List<Chat> result = chatService.queryChats(id);
        return Result.ok(result);
    }

    @GetMapping("/queryChat")
    public Result queryChat(@RequestParam("chatId") Long chatId){
        Chat chat = chatService.getById(chatId);
        return Result.ok(chat);
    }

    @PostMapping("/make")
    public Result makeChat(@RequestBody Chat chat){
        Chat res = chatService.makeChat(chat);
        return Result.ok(res);
    }
}
