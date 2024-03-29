package com.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog.dto.CommentDTO;
import com.example.blog.entity.BlogComment;

import java.util.List;

public interface BlogCommentsService extends IService<BlogComment> {

    List<CommentDTO> queryByBlogId(Long id, Boolean flag);

    CommentDTO addComment(CommentDTO commentDTO);

    List<CommentDTO> queryMyComments(Integer current);

    Boolean delComment(Long id);
}
