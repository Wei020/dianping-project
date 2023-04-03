package com.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.user.dto.ChatDTO;
import com.example.user.dto.UserDTO;
import com.example.user.entity.Chat;
import com.example.user.entity.Group;

import java.util.List;

public interface GroupService extends IService<Group> {
    ChatDTO makeGroup(Group group);

    List<Group> queryGroupByCondition(String condition);

    Chat queryFollowed(Long id);

    List<UserDTO> queryFiveNumbers(Long id);

    void followGroup(Long id);
}
