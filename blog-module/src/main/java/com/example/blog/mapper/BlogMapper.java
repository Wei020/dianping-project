package com.example.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blog.entity.Blog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

}
