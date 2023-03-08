package com.example.user.controller;


import com.example.user.dto.CommentDTO;
import com.example.user.dto.Result;
import com.example.user.entity.BlogComment;
import com.example.user.service.BlogCommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog-comments")
public class BlogCommentsController {

    @Autowired
    private BlogCommentsService blogCommentsService;

    @PostMapping("/add")
    public Result blogComment(@RequestBody BlogComment blogComment){
        CommentDTO commentDTO = blogCommentsService.addComment(blogComment);
        return Result.ok(commentDTO);
    }

    @GetMapping("/query/{id}")
    public Result queryBlogComments(@PathVariable("id") Long id){
        List<CommentDTO> list = blogCommentsService.queryByBlogId(id);
        return Result.ok(list);
    }

}
