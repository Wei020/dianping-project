package com.example.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long Id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long fromId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long toId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long groupId;

    private String content;

    /*
     * 0:文字
     * 1:文件
     * */
    private Integer type;

    private LocalDateTime sendTime;

    private String fromNickname;

    private String fromIcon;

    private Integer state;
}
