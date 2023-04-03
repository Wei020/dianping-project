package com.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.user.dto.ChatDTO;
import com.example.user.dto.MessageDTO;
import com.example.user.dto.Result;
import com.example.user.entity.Chat;
import com.example.user.entity.Group;
import com.example.user.entity.Message;

import java.util.List;

public interface ChatService extends IService<Chat> {

    List<ChatDTO> queryChats(Long id);

    List<Message> queryByChatId(Long chatId);

    Chat makeChat(Chat chat, boolean flag);

    List<MessageDTO> queryNotice(Long id);
}
