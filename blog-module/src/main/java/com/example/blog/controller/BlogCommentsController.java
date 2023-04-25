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
    public Result blogComment(@RequestBody CommentDTO commentDTO){
        CommentDTO res = blogCommentsService.addComment(commentDTO);
        return Result.ok(res);
    }

    @PostMapping("/delete/{id}")
    public Result delBlogComment(@PathVariable Long id){
        Boolean res =  blogCommentsService.delComment(id);
        return Result.ok(res);
    }

    @GetMapping("/query/{id}")
    public Result queryBlogComments(@PathVariable("id") Long id){
        List<CommentDTO> list = blogCommentsService.queryByBlogId(id, true);
        return Result.ok(list);
    }

    @GetMapping("/queryAll/{id}")
    public Result queryAllBlogComments(@PathVariable("id") Long id){
        List<CommentDTO> list = blogCommentsService.queryByBlogId(id, false);
        return Result.ok(list);
    }

    @PostMapping("/comments")
    public Result queryMyComments(@RequestParam("current") Integer current){
        List<CommentDTO> list = blogCommentsService.queryMyComments(current);
        return Result.ok(list);
    }

}
