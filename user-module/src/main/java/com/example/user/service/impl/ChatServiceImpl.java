package com.example.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.user.entity.Blog;
import com.example.user.entity.Message;
import com.example.user.mapper.BlogMapper;
import com.example.user.mapper.ChatMapper;
import com.example.user.service.ChatService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Message> implements ChatService {


    @Override
    public void saveEntity(Message message) {
        if (message.getFromId() == null)
            message.setFromId(1l);
        if (message.getFromNickname() == null)
            message.setFromNickname("hh");
        message.setSendTime(LocalDateTime.now());
        save(message);
    }
}
