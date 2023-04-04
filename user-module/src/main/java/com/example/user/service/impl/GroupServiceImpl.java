package com.example.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.user.dto.ChatDTO;
import com.example.user.dto.MessageDTO;
import com.example.user.dto.NoticeDTO;
import com.example.user.dto.UserDTO;
import com.example.user.entity.*;
import com.example.user.mapper.GroupMapper;
import com.example.user.service.*;
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
    private NoticeService noticeService;

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
        Group group = getById(id);
        Long createId = group.getCreateId();
        Notice notice = new Notice();
        notice.setFromId(user.getId());
        notice.setType(0);
        notice.setToId(createId);
        notice.setGroupId(id);
        notice.setContent(group.getName());
        notice.setSendTime(LocalDateTime.now());
        noticeService.save(notice);
        NoticeDTO noticeDTO = new NoticeDTO();
        BeanUtil.copyProperties(notice, noticeDTO);
        noticeDTO.setFromNickname(user.getNickName());
        noticeDTO.setFromIcon(user.getIcon());
        simpMessagingTemplate.convertAndSendToUser(createId.toString(), "/notice", noticeDTO);
    }
}
