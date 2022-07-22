package com.example.user.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Message {

    private String from;

    private String to;

    private String content;

    private Date createTime;

    private String fromNickname;

    private String fromUserProfile;

    private Integer messageTypeId;
}
