package com.example.chat.controller;



import com.example.chat.entity.Message;
import com.example.chat.service.ChatService;
import com.example.chat.service.MessageService;
import com.example.chat.utils.RedisConstants;
import com.example.chat.utils.ThreadPool;
import com.example.feign.clients.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadPoolExecutor;

@Controller
@Slf4j
public class WsController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @MessageMapping("/hello")
    public void greeting(Message message){
        log.info("传入信息为:{}", message.toString());
        if(message.getFromId() == null)
            message.setFromId(0l);
        message.setSendTime(LocalDateTime.now());
        if(message.getToId() == null)
            message.setToId(0l);
        messageService.save(message);
        ThreadPoolExecutor poolExecutor = ThreadPool.poolExecutor;
        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                chatService.update().eq("id", message.getChatId()).set("update_time", LocalDateTime.now()).update();
                String key = RedisConstants.CHAT_LIST_KEY + message.getFromId();
                stringRedisTemplate.delete(key);
            }
        });
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
        ThreadPoolExecutor poolExecutor = ThreadPool.poolExecutor;
        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                chatService.update().eq("id", message.getChatId()).set("update_time", LocalDateTime.now()).update();
                String key1 = RedisConstants.CHAT_LIST_KEY + message.getFromId();
                String key2 = RedisConstants.CHAT_LIST_KEY + message.getToId();
                stringRedisTemplate.delete(key1);
                stringRedisTemplate.delete(key2);
            }
        });
        simpMessagingTemplate.convertAndSendToUser(message.getToId().toString(), "/chat", message);
    }
}
