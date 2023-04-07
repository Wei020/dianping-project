package com.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog.dto.Result;
import com.example.blog.dto.UserDTO;
import com.example.blog.entity.Follow;

import java.util.List;


public interface FollowService extends IService<Follow> {

    Result follow(Long followUserId, Boolean isFollow);

    Result isFollow(Long followUserId);

    Result followCommons(Long id);

    List<UserDTO> queryFans();

}
