package com.example.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.user.entity.Blog;
import com.example.user.entity.Message;
import com.example.user.entity.User;
import com.example.user.mapper.BlogMapper;
import com.example.user.mapper.ChatMapper;
import com.example.user.service.ChatService;
import com.example.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Message> implements ChatService {

}
