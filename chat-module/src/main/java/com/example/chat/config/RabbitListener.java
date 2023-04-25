package com.example.chat.config;

import com.alibaba.fastjson.JSONObject;
import com.example.chat.dto.NoticeDTO;
import com.example.chat.dto.UserDTO;
import com.example.chat.service.ChatService;
import com.example.chat.service.NoticeService;
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
    private NoticeService noticeService;

    @Autowired
    private ChatService chatService;

    @org.springframework.amqp.rabbit.annotation.RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "notice.queue"),
            exchange = @Exchange(name = "notice.direct"),
            key = "notice"
    ))
    public void listerFollowQueue(NoticeDTO noticeDTO) {
        log.info("取出消息:" + JSONObject.toJSONString(noticeDTO.toString()));
        noticeService.notice(noticeDTO);
    }

    @org.springframework.amqp.rabbit.annotation.RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "info.queue"),
            exchange = @Exchange(name = "info.direct"),
            key = "info"
    ))
    public void listerFollowQueue(UserDTO userDTO) {
        log.info("取出消息:" + JSONObject.toJSONString(userDTO));
        chatService.delChatCache(userDTO);
    }

}
