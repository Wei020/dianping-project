package com.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.user.dto.LoginFormDTO;
import com.example.user.dto.Result;
import com.example.user.entity.User;

import javax.servlet.http.HttpServletRequest;


public interface UserService extends IService<User> {

    Result sendPhoneCode(String phone);

    Result sendEmailCode(String eamil);

    Result login(LoginFormDTO loginForm);

    Result sign();

    Result signCount();

    Result logout(HttpServletRequest request);

    Result queryUserById(Long userId);

    Result info(Long userId);

//    Result queryVoucherByUser();
}
