package com.example.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.dto.CommentDTO;
import com.example.blog.dto.UserDTO;
import com.example.blog.entity.BlogComment;
import com.example.blog.mapper.BlogCommentsMapper;
import com.example.blog.service.BlogCommentsService;
import com.example.feign.clients.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;


@Slf4j
@Service
public class BlogCommentsServiceImpl extends ServiceImpl<BlogCommentsMapper, BlogComment> implements BlogCommentsService {

    @Autowired
    private UserClient userClient;

    @Override
    public List<CommentDTO> queryByBlogId(Long id) {
        List<BlogComment> blogComments = query().eq("blog_id", id).list();
        List<CommentDTO> res = new LinkedList<>();
        for (BlogComment blogComment : blogComments) {
            res.add(extracted(blogComment));
        }
        return res;
    }

    @Override
    public CommentDTO addComment(BlogComment blogComment) {
        save(blogComment);
        CommentDTO commentDTO = extracted(blogComment);
        return commentDTO;
    }

    private CommentDTO extracted(BlogComment blogComment) {
        CommentDTO commentDTO = new CommentDTO();
        BeanUtil.copyProperties(blogComment, commentDTO);
        log.info("commentDTO为:" + commentDTO);
        Object object = userClient.queryUserById(blogComment.getUserId()).getData();
        log.info("查询结果:" + JSONObject.toJSONString(object));
        UserDTO userDTO = JSONObject.parseObject(JSONObject.toJSONString(object), UserDTO.class);
        log.info(userDTO.toString());
        commentDTO.setUser(userDTO);
        return commentDTO;
    }
}
