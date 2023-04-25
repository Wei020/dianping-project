package com.example.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.chat.dto.ChatDTO;
import com.example.chat.dto.UserDTO;
import com.example.chat.entity.Chat;
import com.example.chat.entity.Group;
import com.example.chat.entity.Message;
import com.example.chat.mapper.ChatMapper;
import com.example.chat.service.ChatService;
import com.example.chat.service.GroupService;
import com.example.chat.service.MessageService;
import com.example.chat.utils.RedisConstants;
import com.example.chat.utils.SystemConstants;
import com.example.chat.utils.UserHolder;
import com.example.feign.clients.UserClient;
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
    private UserClient userClient;

    @Autowired
    private GroupService groupService;


    @Override
    public List<ChatDTO> queryChats(Long id, Integer current) {
        String key = RedisConstants.CHAT_LIST_KEY + id;
        String s = stringRedisTemplate.opsForValue().get(key);
        if(null != s && current == 1){
            List<ChatDTO> list = JSONArray.parseArray(s, ChatDTO.class);
            return list;
        }
        List<Chat> list = query().eq("delete_flag", 0)
                .eq("from_id", id).or()
                .eq("to_id", id)
                .orderByDesc("update_time")
                .page(new Page<Chat>(current, SystemConstants.CHAT_PAGE_SIZE)).getRecords();
        if(list == null || list.size() == 0){
            return null;
        }
        List<ChatDTO> res = new LinkedList<>();
        UserDTO userDTO = UserHolder.getUser();
        for (Chat chat : list) {
            ChatDTO chatDTO = getChatDTO(userDTO, chat);
            res.add(chatDTO);
        }
        if(current == 1){
            stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(res));
        }
        return res;
    }

    private ChatDTO getChatDTO(UserDTO userDTO, Chat chat) {
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
                Object data = userClient.queryUserById(chat.getFromId()).getData();
                log.info("RPC查询结果:" + JSONObject.toJSONString(data));
                UserDTO userDTO1 = JSONObject.parseObject(JSONObject.toJSONString(data), UserDTO.class);
                chatDTO.setFromIcon(userDTO1.getIcon());
                chatDTO.setFromNickname(userDTO1.getNickName());
            }
            if(Objects.equals(chat.getToId(), userDTO.getId())){
                chatDTO.setToIcon(userDTO.getIcon());
                chatDTO.setToNickname(userDTO.getNickName());
            }else {
                Object data = userClient.queryUserById(chat.getToId()).getData();
                log.info("RPC查询结果:" + JSONObject.toJSONString(data));
                UserDTO userDTO1 = JSONObject.parseObject(JSONObject.toJSONString(data), UserDTO.class);
                chatDTO.setToIcon(userDTO1.getIcon());
                chatDTO.setToNickname(userDTO1.getNickName());
            }
        }
        return chatDTO;
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
    public ChatDTO makeChat(Chat chat, boolean flag) {
        Long fromId = chat.getFromId();
        Long toId = chat.getToId();
        UserDTO userDTO = UserHolder.getUser();
        if(flag){
            Chat chat1 = query().eq("from_id", fromId).eq("to_id", toId).eq("delete_flag", 0).one();
            Chat chat2 = query().eq("from_id", toId).eq("to_id", fromId).eq("delete_flag", 0).one();
            if(null != chat1) {
                return getChatDTO(userDTO, chat1);
            }
            if(null != chat2)
                return getChatDTO(userDTO, chat2);
        }
        String key1 = RedisConstants.CHAT_LIST_KEY + fromId;
        String key2 = RedisConstants.CHAT_LIST_KEY + toId;
        stringRedisTemplate.delete(key1);
        stringRedisTemplate.delete(key2);
        save(chat);
        log.info("id为:" + chat.getId());
        return getChatDTO(userDTO, chat);
    }

    @Override
    public Boolean delChatCache(UserDTO userDTO) {
        Long id = userDTO.getId();
        List<Chat> chats = query().eq("type", 1)
                .eq("delete_flag", 0)
                .eq("from_id", id)
                .or().eq("to_id", id).list();
        log.info("查到的结果:" + chats.toString());
        for (Chat chat : chats) {
            String key1 = RedisConstants.CHAT_LIST_KEY + chat.getFromId();
            String key2 = RedisConstants.CHAT_LIST_KEY + chat.getToId();
            stringRedisTemplate.delete(key1);
            stringRedisTemplate.delete(key2);
        }
        return true;
    }


}
