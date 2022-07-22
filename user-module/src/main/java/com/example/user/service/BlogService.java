package com.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.user.dto.Result;
import com.example.user.entity.Blog;


public interface BlogService extends IService<Blog> {

    Result queryBlogById(Long id);

    Result queryHotBlog(Integer current);

    Result likeBlog(Long id);

    Result queryBlogLikes(Long id);

    Result saveBlog(Blog blog);

    Result queryBlogOfFollow(Long max, Integer offset);

    Result queryMyBlog(Integer current);
}
