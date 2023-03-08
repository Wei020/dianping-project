package com.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.user.dto.CommentDTO;
import com.example.user.entity.BlogComment;

import java.util.List;

public interface BlogCommentsService extends IService<BlogComment> {

    List<CommentDTO> queryByBlogId(Long id);

    CommentDTO addComment(BlogComment blogComment);
}
