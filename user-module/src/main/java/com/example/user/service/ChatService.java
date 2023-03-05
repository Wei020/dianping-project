package com.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.user.entity.Chat;
import com.example.user.entity.Message;

import java.util.List;

public interface ChatService extends IService<Chat> {

    List<Chat> queryChats(Long id);

    List<Message> queryByChatId(Long chatId);

    Chat makeChat(Chat chat);
}
