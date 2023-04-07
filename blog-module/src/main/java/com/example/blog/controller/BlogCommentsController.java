package com.example.blog.controller;


import com.example.blog.dto.CommentDTO;
import com.example.blog.dto.Result;
import com.example.blog.entity.BlogComment;
import com.example.blog.service.BlogCommentsService;
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

    @PostMapping("/comments")
    public Result queryMyComments(){
        List<CommentDTO> list = blogCommentsService.queryMyComments();
        return Result.ok(list);
    }

}
