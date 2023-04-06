package com.example.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.chat.dto.ChatDTO;
import com.example.chat.entity.Chat;
import com.example.chat.entity.Message;

import java.util.List;

public interface ChatService extends IService<Chat> {

    List<ChatDTO> queryChats(Long id);

    List<Message> queryByChatId(Long chatId);

    Chat makeChat(Chat chat, boolean flag);
}
