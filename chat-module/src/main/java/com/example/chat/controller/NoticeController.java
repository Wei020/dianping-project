package com.example.chat.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.chat.dto.NoticeDTO;
import com.example.chat.dto.Result;
import com.example.chat.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/queryNotice")
    public Result queryNotice(@RequestParam("id") Long id, @RequestParam("current") Integer current){
        List<NoticeDTO> res = noticeService.queryNotice(id, current);
        log.info("查询通知结果:" + JSONObject.toJSONString(res));
        return Result.ok(res);
    }

    @PostMapping("/doNotice")
    public Result doNotice(@RequestParam("id") Long id, @RequestParam("flag") Integer flag){
        log.info("接收参数：" + id +"-" + flag);
        noticeService.doNotice(id, flag);
        return Result.ok();
    }

    @PostMapping
    public Result notice(@RequestBody NoticeDTO noticeDTO){
        noticeService.notice(noticeDTO);
        return Result.ok();
    }
}
