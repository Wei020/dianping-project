package com.example.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blog.entity.BlogComments;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogCommentsMapper extends BaseMapper<BlogComments> {

}
