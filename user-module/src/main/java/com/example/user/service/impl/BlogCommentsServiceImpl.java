package com.example.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.user.dto.CommentDTO;
import com.example.user.dto.UserDTO;
import com.example.user.entity.BlogComment;
import com.example.user.entity.User;
import com.example.user.mapper.BlogCommentsMapper;
import com.example.user.service.BlogCommentsService;
import com.example.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;


@Slf4j
@Service
public class BlogCommentsServiceImpl extends ServiceImpl<BlogCommentsMapper, BlogComment> implements BlogCommentsService {

    @Autowired
    private UserService userService;

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
        log.info("commentDTO为:" + commentDTO.toString());
        User user = userService.getById(blogComment.getUserId());
        UserDTO userDTO = new UserDTO();
        BeanUtil.copyProperties(user, userDTO);
        log.info(userDTO.toString());
        commentDTO.setUser(userDTO);
        return commentDTO;
    }
}
