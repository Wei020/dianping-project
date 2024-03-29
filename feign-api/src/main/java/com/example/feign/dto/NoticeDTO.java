package com.example.feign.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class NoticeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long Id;

    private Long fromId;

    private Long toId;

    private Long groupId;

    private Long blogId;

    private String content;

    /*
     * 0:申请加群
     * 1:申请加人
     * 2:关注通知
     * 3:评论通知
     * */
    private Integer type;

    private LocalDateTime sendTime;

    private LocalDateTime doTime;

    private String fromNickname;

    private String fromIcon;

    private Integer state;
}
