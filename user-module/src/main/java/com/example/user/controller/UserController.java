package com.example.user.controller;

import com.example.user.dto.LoginFormDTO;
import com.example.user.dto.Result;
import com.example.user.dto.UserDTO;
import com.example.user.entity.User;
import com.example.user.service.UserService;
import com.example.user.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
    public Result sendPhoneCode(@RequestParam("phone") String phone) {
        return userService.sendPhoneCode(phone);
    }

    @PostMapping("/emailed")
    public Result sendEmailCode(@RequestParam("email") String eamil) {
        return userService.sendEmailCode(eamil);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginForm){
        return userService.login(loginForm);
    }

    @PostMapping("/logout")
    public Result logout(HttpServletRequest request){
        return userService.logout(request);
    }

    @GetMapping("/me")
    public Result me(){
        UserDTO user = UserHolder.getUser();
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

//    @GetMapping("/my-voucher")
//    public Result queryVoucherByUser(){
//        return userService.queryVoucherByUser();
//    }

//    @GetMapping("/{id}")
//    private User getById(@PathVariable("id") Long id){
//        return userService.getById(id);
//    }
    @PostMapping("/list")
    public List<User> query(@RequestParam List<Long> ids, @RequestParam String idStr){
        return userService.query().in("id", ids).last(StringUtils.isNotBlank(idStr),"order by field(id, " + idStr + ")").list();
    }
}
