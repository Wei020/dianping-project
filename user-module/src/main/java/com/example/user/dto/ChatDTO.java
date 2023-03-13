package com.example.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class ChatDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private Long fromId;

    private String fromNickname;

    private String fromIcon;

    private Long toId;

    private String toNickname;

    private String toIcon;

    /*
     * 0:群聊
     * 1:私聊
     * */
    private Integer type;
}
