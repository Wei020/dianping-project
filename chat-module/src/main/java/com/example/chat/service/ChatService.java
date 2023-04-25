package com.example.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.chat.dto.ChatDTO;
import com.example.chat.dto.UserDTO;
import com.example.chat.entity.Chat;
import com.example.chat.entity.Message;

import java.util.List;

public interface ChatService extends IService<Chat> {

    List<ChatDTO> queryChats(Long id, Integer current);

    List<Message> queryByChatId(Long chatId);

    ChatDTO makeChat(Chat chat, boolean flag);

    Boolean delChatCache(UserDTO userDTO);
}
