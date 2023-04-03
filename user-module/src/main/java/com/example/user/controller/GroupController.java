package com.example.user.controller;

import com.example.user.dto.ChatDTO;
import com.example.user.dto.Result;
import com.example.user.dto.UserDTO;
import com.example.user.entity.Chat;
import com.example.user.entity.Group;
import com.example.user.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/makeGroup")
    public Result makeGroup(@RequestBody Group group){
        ChatDTO res = groupService.makeGroup(group);
        return Result.ok(res);
    }

    @GetMapping("/find")
    public Result queryGroupByCondition(@RequestParam("condition") String condition){
        List<Group> res = groupService.queryGroupByCondition(condition);
        return Result.ok(res);
    }

    @GetMapping("/{id}")
    public Result queryGroupById(@PathVariable("id") Long id){
        Group group = groupService.getById(id);
        return Result.ok(group);
    }

    @GetMapping("/followed/{id}")
    public Result queryFollowed(@PathVariable("id") Long id){
        Chat chat = groupService.queryFollowed(id);
        return Result.ok(chat);
    }

    @GetMapping("/five/{id}")
    public Result queryFiveNumbers(@PathVariable("id") Long id){
        List<UserDTO> list = groupService.queryFiveNumbers(id);
        return Result.ok(list);
    }

    @PostMapping("/follow/{id}")
    public Result followGroup(@PathVariable("id") Long id){
        groupService.followGroup(id);
        return Result.ok();
    }
}
