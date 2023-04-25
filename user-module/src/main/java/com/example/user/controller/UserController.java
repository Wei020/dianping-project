package com.example.user.controller;

import com.example.feign.dto.UserListDTO;
import com.example.user.dto.*;
import com.example.user.entity.User;
import com.example.user.entity.UserInfo;
import com.example.user.service.UserService;
import com.example.user.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    @PostMapping("/phoned")
    public synchronized Result sendPhoneCode(@RequestParam("phone") String phone) {
        return userService.sendPhoneCode(phone);
    }

    @PostMapping("/emailed")
    public Result sendEmailCode(@RequestParam("email") String email) {
        return userService.sendEmailCode(email);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginForm){
        return userService.login(loginForm);
    }

    @PostMapping("/findPassword")
    public Result findPassword(@RequestBody LoginFormDTO loginForm){
        return userService.findPassword(loginForm);
    }

    @PostMapping("/logout/{id}")
    public Result logout(@PathVariable("id") Long id, HttpServletRequest request){
        return userService.logout(id, request);
    }

    @GetMapping("/me")
    public Result me(){
        UserDTO user = UserHolder.getUser();
        log.info("获取的名字:" + user.getNickName());
        return Result.ok(user);
    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long userId){
        return userService.info(userId);
    }

    @GetMapping("/{id}")
    public Result queryUserById(@PathVariable("id") Long userId){
        return userService.queryUserById(userId);
    }


    @PostMapping("/sign")
    public Result sign(){
        return userService.sign();
    }

    @GetMapping("/sign/count")
    public Result signCount(){
        return userService.signCount();
    }

    @GetMapping("/my-voucher")
    public Result queryVoucherByUser(){
        return userService.queryVoucherByUser();
    }

    @PostMapping("/edit")
    public Result editUser(@RequestBody User user, HttpServletRequest request){
        return userService.edit(user, request);
    }

    @PostMapping("/infoEdit")
    public Result infoEdit(@RequestBody UserInfo userInfo){
        return userService.infoEdit(userInfo);
    }

    @PostMapping("/follow")
    public Result followUser(@RequestParam("id") Long id, @RequestParam("followId") Long followId){
        Boolean res =  userService.followUser(id, followId);
        return Result.ok(res);
    }

    @PostMapping("/notFollow")
    public Result notFollowUser(@RequestParam("id") Long id, @RequestParam("followId") Long followId){
        Boolean res =  userService.notFollowUser(id, followId);
        return Result.ok(res);
    }

    @PostMapping("/list")
    public Result queryUserList(@RequestBody UserListDTO userListDTO){
        return userService.queryUserList(userListDTO);
    }

    @GetMapping("/find")
    public Result queryUserByCondition(@RequestParam("condition") String condition){
        return userService.queryUserByCondition(condition);
    }

    @PostMapping("/city")
    public Result queryCity(){
        List<OptionsDTO> provinces = userService.queryCity();
        return Result.ok(provinces);
    }
}
