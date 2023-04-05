package com.example.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.user.dto.*;
import com.example.user.entity.*;
import com.example.user.mapper.ChatMapper;
import com.example.user.service.*;
import com.example.user.utils.RedisConstants;
import com.example.user.utils.RegexUtils;
import com.example.user.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements ChatService {

    @Autowired
    private MessageService messageService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;


    @Override
    public List<ChatDTO> queryChats(Long id) {
        String key = RedisConstants.CHAT_LIST_KEY + id;
        String s = stringRedisTemplate.opsForValue().get(key);
        if(null != s){
            List<ChatDTO> list = JSONArray.parseArray(s, ChatDTO.class);
            return list;
        }
        List<Chat> list = query().eq("delete_flag", 0).eq("from_id", id).or().eq("to_id", id).list();
        List<ChatDTO> res = new LinkedList<>();
        UserDTO userDTO = UserHolder.getUser();
        for (Chat chat : list) {
            ChatDTO chatDTO = BeanUtil.copyProperties(chat, ChatDTO.class);
            if(chat.getType() == 0){
                chatDTO.setFromIcon(userDTO.getIcon());
                chatDTO.setFromNickname(userDTO.getNickName());
                Group group = groupService.getById(chat.getToId());
                chatDTO.setToNickname(group.getName());
                chatDTO.setToIcon(group.getIcon());
            }else if(chat.getType() == 1){
                if(Objects.equals(chat.getFromId(), userDTO.getId())){
                    chatDTO.setFromIcon(userDTO.getIcon());
                    chatDTO.setFromNickname(userDTO.getNickName());
                }else {
                    User user = userService.query().eq("id", chat.getFromId()).one();
                    chatDTO.setFromIcon(user.getIcon());
                    chatDTO.setFromNickname(user.getNickName());
                }
                if(Objects.equals(chat.getToId(), userDTO.getId())){
                    chatDTO.setToIcon(userDTO.getIcon());
                    chatDTO.setToNickname(userDTO.getNickName());
                }else {
                    User user = userService.query().eq("id", chat.getToId()).one();
                    chatDTO.setToIcon(user.getIcon());
                    chatDTO.setToNickname(user.getNickName());
                }
            }
            res.add(chatDTO);
        }
        stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(res));
        return res;
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
    public Chat makeChat(Chat chat, boolean flag) {
        Long fromId = chat.getFromId();
        Long toId = chat.getToId();
        if(flag){
            Chat chat1 = query().eq("from_id", fromId).eq("to_id", toId).eq("delete_flag", 0).one();
            Chat chat2 = query().eq("from_id", toId).eq("to_id", fromId).eq("delete_flag", 0).one();
            if(null != chat1)
                return chat1;
            if(null != chat2)
                return chat2;
        }
        String key1 = RedisConstants.CHAT_LIST_KEY + fromId;
        String key2 = RedisConstants.CHAT_LIST_KEY + toId;
        stringRedisTemplate.delete(key1);
        stringRedisTemplate.delete(key2);
        save(chat);
        log.info("id为:" + chat.getId());
        return chat;
    }
}
