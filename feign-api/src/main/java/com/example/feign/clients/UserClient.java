package com.example.feign.clients;


import com.example.feign.dto.Result;
import com.example.feign.dto.UserListDTO;
import com.example.feign.fallback.UserClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "userservice",fallbackFactory = UserClientFallbackFactory.class)
public interface UserClient {

    @GetMapping("/user/{id}")
    Result queryUserById(@PathVariable("id") Long id);

    @PostMapping("/user/list")
    Result queryUserList(@RequestBody UserListDTO userList);

    @PostMapping("/user/follow")
    Result followUser(@RequestParam("id") Long id, @RequestParam("followId") Long followId);

    @PostMapping("/user/notFollow")
    Result notFollowUser(@RequestParam("id") Long id, @RequestParam("followId") Long followId);
}
