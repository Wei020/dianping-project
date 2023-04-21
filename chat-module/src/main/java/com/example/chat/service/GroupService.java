package com.example.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.chat.dto.ChatDTO;
import com.example.chat.dto.UserDTO;
import com.example.chat.entity.Chat;
import com.example.chat.entity.Group;

import java.util.List;

public interface GroupService extends IService<Group> {
    ChatDTO makeGroup(Group group);

    List<Group> queryGroupByCondition(String condition);

    Chat queryFollowed(Long id);

    List<UserDTO> queryFiveNumbers(Long id);

    void followGroup(Long id);

    Group outGroup(Long id);

    List<UserDTO> queryNumbers(Long id);

    Boolean cancelGroup(Long id);
}
