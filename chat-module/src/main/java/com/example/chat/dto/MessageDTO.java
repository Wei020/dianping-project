package com.example.chat.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDTO {

    private Long Id;

    private Long fromId;

    private Long toId;

    private String content;

    /*
     * 0:文字
     * 1:文件
     * */
    private Integer type;

    private LocalDateTime sendTime;

    private String fromNickname;

    private String fromIcon;

    private String toNickname;

    private String toIcon;

    private Long chatId;
}
