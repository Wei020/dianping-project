package com.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog.dto.Result;
import com.example.blog.entity.Blog;

import java.util.List;


public interface BlogService extends IService<Blog> {

    Result queryBlogById(Long id);

    Result queryHotBlog(Integer current);

    Result likeBlog(Long id);

    Result queryBlogLikes(Long id);

    Result saveBlog(Blog blog);

    Result queryBlogOfFollow(Long max, Integer offset);

    Result queryMyBlog(Integer current);

    Boolean deleteBlog(Long id);

    List<Blog> queryBlogs(String condition);

}
