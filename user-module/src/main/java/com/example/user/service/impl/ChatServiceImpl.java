package com.example.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.user.entity.Blog;
import com.example.user.entity.Chat;
import com.example.user.entity.Message;
import com.example.user.entity.User;
import com.example.user.mapper.BlogMapper;
import com.example.user.mapper.ChatMapper;
import com.example.user.service.ChatService;
import com.example.user.service.MessageService;
import com.example.user.service.UserService;
import com.example.user.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements ChatService {

    @Autowired
    private MessageService messageService;


    @Override
    public List<Chat> queryChats(Long id) {
        List<Chat> list = query().eq("from_id", id).or().eq("to_id", id).list();
        return list;
    }

    @Override
    public List<Message> queryByChatId(Long chatId) {
        Chat chat = getById(chatId);
        Integer type = chat.getType();
        List<Message> result;
        if(type == 0){
            result = messageService.query().eq("to_id", chat.getToId()).list();
        }else{
            result = messageService.query().eq("chat_id", chatId).list();
        }
        return result;
    }

    @Override
    public Chat makeChat(Chat chat) {
        Long fromId = chat.getFromId();
        Long toId = chat.getToId();
        Chat chat1 = query().eq("from_id", fromId).eq("to_id", toId).one();
        Chat chat2 = query().eq("from_id", toId).eq("to_id", fromId).one();
        if(null != chat1)
            return chat1;
        if(null != chat2)
            return chat2;
        save(chat);
        log.info("id为:" + chat.getId());
        return chat;
    }
}
