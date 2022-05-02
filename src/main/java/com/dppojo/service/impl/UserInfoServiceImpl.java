package com.dppojo.service.impl;

import com.dppojo.entity.UserInfo;
import com.dppojo.mapper.UserInfoMapper;
import com.dppojo.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
