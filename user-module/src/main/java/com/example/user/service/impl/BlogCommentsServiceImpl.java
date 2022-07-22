package com.example.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.user.entity.BlogComments;
import com.example.user.mapper.BlogCommentsMapper;
import com.example.user.service.BlogCommentsService;
import org.springframework.stereotype.Service;


@Service
public class BlogCommentsServiceImpl extends ServiceImpl<BlogCommentsMapper, BlogComments> implements BlogCommentsService {

}
