package com.example.blog.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FollowDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long followUserId;

    private Boolean isFollow;

    public FollowDTO(Long id, Long followUserId, boolean isFollow) {
        this.userId = id;
        this.followUserId = followUserId;
        this.isFollow = isFollow;
    }
}
