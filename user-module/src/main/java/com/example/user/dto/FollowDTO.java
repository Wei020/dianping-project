package com.example.user.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FollowDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long followUserId;

    private Boolean isFollow;
}
