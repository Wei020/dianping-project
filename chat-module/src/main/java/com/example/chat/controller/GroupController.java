package com.example.chat.controller;

import com.example.chat.dto.ChatDTO;
import com.example.chat.dto.Result;
import com.example.chat.dto.UserDTO;
import com.example.chat.entity.Chat;
import com.example.chat.entity.Group;
import com.example.chat.service.GroupService;
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

    @PostMapping("/all/{id}")
    public Result queryNumbers(@PathVariable("id") Long id){
        log.info("传入的id:" + id);
        List<UserDTO> res = groupService.queryNumbers(id);
        return Result.ok(res);
    }

    @PostMapping("/follow/{id}")
    public Result followGroup(@PathVariable("id") Long id){
        groupService.followGroup(id);
        return Result.ok();
    }

    @PostMapping("/out/{id}")
    public Result outGroup(@PathVariable("id") Long id){
        Group group = groupService.outGroup(id);
        return Result.ok(group);
    }

    @PostMapping("/cancel/{id}")
    public Result cancelGroup(@PathVariable("id") Long id){
        Boolean res = groupService.cancelGroup(id);
        return Result.ok(res);
    }
}
