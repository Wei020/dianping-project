package com.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.user.entity.UserInfo;


public interface UserInfoService extends IService<UserInfo> {

    boolean saveUserInfo(Long id);


}
