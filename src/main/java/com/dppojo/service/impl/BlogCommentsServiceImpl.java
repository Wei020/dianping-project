package com.dppojo.service.impl;

import com.dppojo.entity.BlogComments;
import com.dppojo.mapper.BlogCommentsMapper;
import com.dppojo.service.IBlogCommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class BlogCommentsServiceImpl extends ServiceImpl<BlogCommentsMapper, BlogComments> implements IBlogCommentsService {

}
