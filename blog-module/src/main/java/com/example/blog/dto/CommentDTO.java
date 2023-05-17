package com.example.blog.dto;

import com.example.blog.entity.Blog;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentDTO {

    /**
     * 主键
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    /**
     * 用户信息
     */
    private UserDTO user;

    /**
     * 探店id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long blogId;


    private Blog blog;

    /**
     * 关联的1级评论id，如果是一级评论，则值为0
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;

    private List<CommentDTO> respondLists;


    private UserDTO answerUser;

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

    /**
     * 状态，0：正常，1：被举报，2：禁止查看
     */
    private Boolean status;

    private Integer type;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
