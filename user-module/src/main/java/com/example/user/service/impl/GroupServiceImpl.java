package com.example.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.user.dto.ChatDTO;
import com.example.user.dto.MessageDTO;
import com.example.user.dto.UserDTO;
import com.example.user.entity.Chat;
import com.example.user.entity.Group;
import com.example.user.entity.Message;
import com.example.user.entity.User;
import com.example.user.mapper.GroupMapper;
import com.example.user.service.ChatService;
import com.example.user.service.GroupService;
import com.example.user.service.MessageService;
import com.example.user.service.UserService;
import com.example.user.utils.RedisConstants;
import com.example.user.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageService messageService;

    @Override
    public ChatDTO makeGroup(Group group) {
        boolean save = save(group);
        if(save){
            Chat chat = new Chat();
            chat.setType(0);
            chat.setFromId(group.getCreateId());
            chat.setToId(group.getId());
            Chat res = chatService.makeChat(chat, false);
            ChatDTO chatDTO = BeanUtil.copyProperties(res, ChatDTO.class);
            chatDTO.setToNickname(group.getName());
            chatDTO.setToIcon(group.getIcon());
            UserDTO userDTO = UserHolder.getUser();
            chatDTO.setFromNickname(userDTO.getNickName());
            chatDTO.setFromIcon(userDTO.getIcon());
            return chatDTO;
        }
        return null;
    }

    @Override
    public List<Group> queryGroupByCondition(String condition) {
        List<Group> res = query().like("name", condition).list();
        return res;
    }

    @Override
    public Chat queryFollowed(Long id) {
        Long userId = UserHolder.getUser().getId();
        Chat chat = chatService.query().eq("from_id", userId).eq("to_id", id).one();
        return chat;
    }

    @Override
    public List<UserDTO> queryFiveNumbers(Long id) {
        String key = RedisConstants.GROUP_FIVE_KEY + id;
        String s = stringRedisTemplate.opsForValue().get(key);
        if(null != s){
            List<UserDTO> res = JSONObject.parseArray(s, UserDTO.class);
            return res;
        }
        List<Chat> list = chatService.query().eq("to_id", id).orderByAsc("create_time").last("limit 0, 5").list();
        List<UserDTO> res = new LinkedList<>();
        for (Chat chat : list) {
            UserDTO userDTO = new UserDTO();
            User user = userService.query().eq("id", chat.getFromId()).one();
            BeanUtil.copyProperties(user, userDTO);
            res.add(userDTO);
        }
        stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(res));
        log.info("查询结果:" + JSONObject.toJSONString(res));
        return res;
    }

    @Override
    public void followGroup(Long id) {
        UserDTO user = UserHolder.getUser();
//        Chat chat = new Chat();
//        chat.setState(0);
//        chat.setType(0);
//        chat.setFromId(userId);
//        chat.setToId(id);
//        chatService.save(chat);
        Group group = getById(id);
        Long createId = group.getCreateId();
        Message message = new Message();
        message.setSendTime(LocalDateTime.now());
        message.setType(2);
        message.setFromId(user.getId());
        message.setToId(createId);
        message.setContent(id.toString());
        messageService.save(message);
        MessageDTO messageDTO = new MessageDTO();
        BeanUtil.copyProperties(message, messageDTO);
        messageDTO.setFromNickname(user.getNickName());
        messageDTO.setFromIcon(user.getIcon());
        messageDTO.setToNickname(group.getName());
        simpMessagingTemplate.convertAndSendToUser(createId.toString(), "/chat", messageDTO);
    }
}
