package com.dppojo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dppojo.dto.LoginFormDTO;
import com.dppojo.dto.Result;
import com.dppojo.entity.User;


public interface IUserService extends IService<User> {

    Result sendPhoneCode(String phone);

    Result sendEmailCode(String eamil);

    Result login(LoginFormDTO loginForm);

    Result sign();

    Result signCount();

    Result logout();

    Result queryUserById(Long userId);

    Result info(Long userId);
}
