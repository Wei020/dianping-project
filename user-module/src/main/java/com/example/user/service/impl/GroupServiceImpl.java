package com.example.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.user.dto.ChatDTO;
import com.example.user.dto.Result;
import com.example.user.dto.UserDTO;
import com.example.user.entity.Chat;
import com.example.user.entity.Group;
import com.example.user.mapper.GroupMapper;
import com.example.user.service.ChatService;
import com.example.user.service.GroupService;
import com.example.user.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

    @Autowired
    private ChatService chatService;

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
}
