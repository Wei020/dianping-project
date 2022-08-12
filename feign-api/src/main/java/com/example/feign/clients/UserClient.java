package com.example.feign.clients;


import com.example.feign.dto.Result;
import com.example.feign.entity.User;
import com.example.feign.fallback.UserClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@FeignClient(value = "userservice",fallbackFactory = UserClientFallbackFactory.class)
public interface UserClient {

    @GetMapping("/user/{id}")
    Result queryUserById(@PathVariable Long id);

    @PostMapping("/user/list")
    List<User> query(@RequestParam List<Long> ids, @RequestParam String idStr);

}
