package com.example.shop.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShopCommentDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 用户id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    private UserDTO user;

    /**
     * 探店id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;

    /**
     * 关联的1级评论id，如果是一级评论，则值为0
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;

    /**
     * 回复的评论id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long answerId;

    /**
     * 回复的内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer liked;

    private Boolean isLike;

    private String images;

    /**
     * 评分
     */
    private Integer rate;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    private Integer deleteFlag;
}
