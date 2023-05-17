package com.example.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.chat.dto.NoticeDTO;
import com.example.chat.dto.UserDTO;
import com.example.chat.entity.Chat;
import com.example.chat.entity.Group;
import com.example.chat.entity.Notice;
import com.example.chat.mapper.NoticeMapper;
import com.example.chat.service.ChatService;
import com.example.chat.service.GroupService;
import com.example.chat.service.NoticeService;
import com.example.chat.utils.RedisConstants;
import com.example.chat.utils.SystemConstants;
import com.example.chat.utils.ThreadPool;
import com.example.chat.utils.UserHolder;
import com.example.feign.clients.UserClient;
import com.example.feign.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<NoticeDTO> queryNotice(Long id, Integer current) {
        String key = RedisConstants.NOTICE_LIST_KEY + id;
        String s = stringRedisTemplate.opsForValue().get(key);
        String readKey = RedisConstants.USER_ISREAD_KEY + id;
        if(null != s && current == 1){
            return JSONObject.parseArray(s, NoticeDTO.class);
        }
        log.info("current：" + current);
        List<Notice> list = query().eq("to_id", id).or().eq("from_id", id).orderByDesc("send_time")
                .page(new Page<>(current, SystemConstants.NOTICE_PAGE_SIZE)).getRecords();
        if(list == null || list.size() == 0){
            return null;
        }
        log.info("分页结果:" + list.toString());
        List<NoticeDTO> res = new LinkedList<>();
        for (Notice notice : list) {
//            if((notice.getType() == 2 || notice.getType() == 3) && Objects.equals(notice.getFromId(), id))
//                continue;
            NoticeDTO noticeDTO = new NoticeDTO();
            BeanUtil.copyProperties(notice, noticeDTO);
            Long fromId = notice.getFromId();
            Object data = userClient.queryUserById(fromId).getData();
            log.info("RPC查询结果:" + JSONObject.toJSONString(data));
            UserDTO userDTO = JSONObject.parseObject(JSONObject.toJSONString(data), UserDTO.class);
            noticeDTO.setFromIcon(userDTO.getIcon());
            noticeDTO.setFromNickname(userDTO.getNickName());
            if(notice.getType() == 0){
                Long groupId = notice.getGroupId();
                Group group = groupService.getById(groupId);
                noticeDTO.setContent(group.getName());
            }
            res.add(noticeDTO);
        }
        if(current == 1){
            stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(res));
        }
        stringRedisTemplate.opsForValue().set(readKey, "0");
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doNotice(Long id, Integer flag) {
        Notice notice = getById(id);
        String noticeKey1 = RedisConstants.NOTICE_LIST_KEY + notice.getFromId();
        String noticeKey2 = RedisConstants.NOTICE_LIST_KEY + notice.getToId();
        if(flag == 1){
            notice.setState(1);
            String chatKey = RedisConstants.CHAT_LIST_KEY + notice.getFromId();
            if(notice.getType() == 0){
                Chat chat = new Chat();
                chat.setFromId(notice.getFromId());
                chat.setType(0);
                chat.setToId(notice.getGroupId());
                chatService.save(chat);
                stringRedisTemplate.delete(chatKey);
                ThreadPoolExecutor poolExecutor = ThreadPool.poolExecutor;
                poolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Group group = groupService.getById(notice.getGroupId());
                        group.setNumber(group.getNumber() + 1);
                        groupService.updateById(group);
                    }
                });
            }
        }else if(flag == 0){
            notice.setState(2);
        }
        notice.setDoTime(LocalDateTime.now());
        updateById(notice);
        stringRedisTemplate.delete(noticeKey1);
        stringRedisTemplate.delete(noticeKey2);
    }

    @Override
    public void notice(NoticeDTO noticeDTO) {
        String key1 = RedisConstants.NOTICE_LIST_KEY + noticeDTO.getToId();
        String key2 = RedisConstants.NOTICE_LIST_KEY + noticeDTO.getFromId();
        String readKey = RedisConstants.USER_ISREAD_KEY + noticeDTO.getToId();
        noticeDTO.setSendTime(LocalDateTime.now());
        Notice notice = BeanUtil.copyProperties(noticeDTO, Notice.class);
        log.info("存入的notice:" + notice.toString());
        boolean res = save(notice);
        if(res){
            simpMessagingTemplate.convertAndSendToUser(noticeDTO.getToId().toString(), "/notice", noticeDTO);
            stringRedisTemplate.delete(key1);
            if(noticeDTO.getType() == 1 || noticeDTO.getType() == 0){
                stringRedisTemplate.delete(key2);
            }
            stringRedisTemplate.opsForValue().increment(readKey);
        }
    }

    @Override
    public Integer queryNoticeNum() {
        String key = RedisConstants.USER_ISREAD_KEY + UserHolder.getUser().getId();
        String val = stringRedisTemplate.opsForValue().get(key);
        if(val == null || val.isEmpty()){
            return 0;
        }
        Integer num = Integer.parseInt(val);
        return num;
    }
}
