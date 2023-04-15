package com.example.user.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.user.dto.FollowDTO;
import com.example.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitListener {

    @Autowired
    private UserService userService;

    @org.springframework.amqp.rabbit.annotation.RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "follow.queue"),
            exchange = @Exchange(name = "follow.direct"),
            key = "follow"
    ))
    public void listerFollowQueue(FollowDTO followDTO) {
        log.info("取出消息:" + JSONObject.toJSONString(followDTO));
        if (followDTO.getIsFollow()) userService.followUser(followDTO.getUserId(), followDTO.getFollowUserId());
        else userService.notFollowUser(followDTO.getUserId(), followDTO.getFollowUserId());
    }
}
