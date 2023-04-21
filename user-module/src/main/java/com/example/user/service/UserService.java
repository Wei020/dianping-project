package com.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.feign.dto.UserListDTO;
import com.example.user.dto.LoginFormDTO;
import com.example.user.dto.OptionsDTO;
import com.example.user.dto.ProvinceDTO;
import com.example.user.dto.Result;
import com.example.user.entity.User;
import com.example.user.entity.UserInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface UserService extends IService<User> {

    Result sendPhoneCode(String phone);

    Result sendEmailCode(String eamil);

    Result login(LoginFormDTO loginForm);

    Result sign();

    Result signCount();

    Result logout(HttpServletRequest request);

    Result queryUserById(Long userId);

    Result info(Long userId);

    Result queryVoucherByUser();

    Result edit(User user, HttpServletRequest request);

    Result infoEdit(UserInfo userInfo);

    Result findPassword(LoginFormDTO loginForm);

    Result queryUserList(UserListDTO userListDTO);

    Result queryUserByCondition(String condition);

    Boolean followUser(Long id, Long followId);

    Boolean notFollowUser(Long id, Long followId);

    List<OptionsDTO> queryCity();
}
