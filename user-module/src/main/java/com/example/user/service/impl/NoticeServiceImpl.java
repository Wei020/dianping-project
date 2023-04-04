package com.example.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.user.dto.NoticeDTO;
import com.example.user.entity.Chat;
import com.example.user.entity.Group;
import com.example.user.entity.Notice;
import com.example.user.entity.User;
import com.example.user.mapper.NoticeMapper;
import com.example.user.service.ChatService;
import com.example.user.service.GroupService;
import com.example.user.service.NoticeService;
import com.example.user.service.UserService;
import com.example.user.utils.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<NoticeDTO> queryNotice(Long id) {
        List<Notice> list = query().eq("to_id", id).list();
        List<NoticeDTO> res = new LinkedList<>();
        for (Notice notice : list) {
            NoticeDTO noticeDTO = new NoticeDTO();
            BeanUtil.copyProperties(notice, noticeDTO);
            Long fromId = notice.getFromId();
            User user = userService.getById(fromId);
            noticeDTO.setFromIcon(user.getIcon());
            noticeDTO.setFromNickname(user.getNickName());
            if(notice.getType() == 0){
                Long groupId = notice.getGroupId();
                Group group = groupService.getById(groupId);
                noticeDTO.setContent(group.getName());
            }
            res.add(noticeDTO);
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doNotice(Long id, Integer flag) {
        Notice notice = getById(id);
        if(flag == 1){
            notice.setState(1);
            String key = RedisConstants.CHAT_LIST_KEY + notice.getFromId();
            if(notice.getType() == 0){
                Chat chat = new Chat();
                chat.setFromId(notice.getFromId());
                chat.setType(0);
                chat.setToId(notice.getGroupId());
                chatService.save(chat);
                stringRedisTemplate.delete(key);
            }
        }else if(flag == 0){
            notice.setState(2);
        }
        updateById(notice);
    }
}
