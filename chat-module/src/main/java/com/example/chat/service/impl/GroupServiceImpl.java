package com.example.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.chat.dto.ChatDTO;
import com.example.chat.dto.NoticeDTO;
import com.example.chat.dto.UserDTO;
import com.example.chat.entity.Chat;
import com.example.chat.entity.Group;
import com.example.chat.entity.Notice;
import com.example.chat.mapper.GroupMapper;
import com.example.chat.service.ChatService;
import com.example.chat.service.GroupService;
import com.example.chat.service.NoticeService;
import com.example.chat.utils.RedisConstants;
import com.example.chat.utils.ThreadPool;
import com.example.chat.utils.UserHolder;
import com.example.feign.clients.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserClient userClient;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public ChatDTO makeGroup(Group group) {
        if(group.getId() != null){
            updateById(group);
            ThreadPoolExecutor poolExecutor = ThreadPool.poolExecutor;
            poolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    List<Chat> chats = chatService.query().eq("to_id", group.getId()).eq("delete_flag", 0).list();
                    for (Chat chat : chats) {
                        String key = RedisConstants.CHAT_LIST_KEY + chat.getFromId();
                        stringRedisTemplate.delete(key);
                    }
                }
            });
            return null;
        }
        boolean save = save(group);
        if(save){
            Chat chat = new Chat();
            chat.setType(0);
            chat.setFromId(group.getCreateId());
            chat.setToId(group.getId());
            return chatService.makeChat(chat, false);
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
        Chat chat = chatService.query().eq("from_id", userId).eq("to_id", id).eq("delete_flag", 0).one();
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
        List<Chat> list = chatService.query().eq("type", 0).eq("to_id", id).eq("delete_flag", 0)
                .orderByAsc("create_time").last("limit 0, 5").list();
        List<UserDTO> res = new LinkedList<>();
        for (Chat chat : list) {
            Object data = userClient.queryUserById(chat.getFromId()).getData();
            log.info("RPC查询结果:" + JSONObject.toJSONString(data));
            UserDTO userDTO = JSONObject.parseObject(JSONObject.toJSONString(data), UserDTO.class);
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
        String noticeKey1 = RedisConstants.NOTICE_LIST_KEY + createId;
        String noticeKey2 = RedisConstants.NOTICE_LIST_KEY + user.getId();
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
        stringRedisTemplate.delete(noticeKey1);
        stringRedisTemplate.delete(noticeKey2);
    }

    @Override
    public Group outGroup(Long id) {
        UserDTO user = UserHolder.getUser();
        String chatKey = RedisConstants.CHAT_LIST_KEY + user.getId();
        UpdateWrapper<Chat> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("from_id", user.getId());
        updateWrapper.eq("to_id", id);
        updateWrapper.set("delete_flag", 1);
        chatService.update(updateWrapper);
        stringRedisTemplate.delete(chatKey);
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                UpdateWrapper<Message> updateWrapper1 = new UpdateWrapper<>();
//                updateWrapper1.eq("from_id", user.getId());
//                updateWrapper1.eq("to_id", id);
//                updateWrapper1.set("delete_flag", 1);
//                messageService.update(updateWrapper1);
//            }
//        });
//        thread.start();
        Group group = getById(id);
        group.setNumber(group.getNumber() - 1);
        updateById(group);
        return group;
    }

    @Override
    public List<UserDTO> queryNumbers(Long id) {
        List<Chat> list = chatService.query().eq("to_id", id).eq("delete_flag", 0).list();
        List<UserDTO> res = new LinkedList<>();
        for (Chat chat : list) {
            Object data = userClient.queryUserById(chat.getFromId()).getData();
            log.info("RPC查询结果:" + JSONObject.toJSONString(data));
            UserDTO userDTO = JSONObject.parseObject(JSONObject.toJSONString(data), UserDTO.class);
            res.add(userDTO);
        }
        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean cancelGroup(Long id) {
        UpdateWrapper<Group> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("delete_flag", 1);
        boolean update = update(updateWrapper);
        if (update){
            List<Chat> chats = chatService.query().eq("to_id", id).list();
            ThreadPoolExecutor poolExecutor = ThreadPool.poolExecutor;
            for (Chat chat : chats) {
                poolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        String key = RedisConstants.CHAT_LIST_KEY + chat.getFromId();
                        chat.setDeleteFlag(1);
                        chatService.updateById(chat);
                        stringRedisTemplate.delete(key);
                    }
                });
            }
        }
        return update;
    }
}
