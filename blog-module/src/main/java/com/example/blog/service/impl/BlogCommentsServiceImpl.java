package com.example.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.dto.CommentDTO;
import com.example.blog.dto.UserDTO;
import com.example.blog.entity.Blog;
import com.example.blog.entity.BlogComment;
import com.example.blog.mapper.BlogCommentsMapper;
import com.example.blog.service.BlogCommentsService;
import com.example.blog.service.BlogService;
import com.example.blog.utils.RabbitMQUtils;
import com.example.blog.utils.RedisConstants;
import com.example.blog.utils.ThreadPool;
import com.example.blog.utils.UserHolder;
import com.example.feign.clients.ChatClient;
import com.example.feign.clients.UserClient;
import com.example.feign.dto.NoticeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;


@Slf4j
@Service
public class BlogCommentsServiceImpl extends ServiceImpl<BlogCommentsMapper, BlogComment> implements BlogCommentsService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private BlogService blogService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private RabbitMQUtils rabbitMQUtils;

    @Override
    public List<CommentDTO> queryByBlogId(Long id) {
        List<BlogComment> blogComments = query().eq("blog_id", id).eq("delete_flag", 0).list();
        List<CommentDTO> res = new LinkedList<>();
        for (BlogComment blogComment : blogComments) {
            res.add(extracted(blogComment, false));
        }
        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommentDTO addComment(BlogComment blogComment) {
        save(blogComment);
        ThreadPoolExecutor poolExecutor = ThreadPool.poolExecutor;
        UserDTO userDTO = UserHolder.getUser();
        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                String key = RedisConstants.MY_COMMENTS_KEY + userDTO.getId();
                NoticeDTO noticeDTO = new NoticeDTO();
                Blog blog = blogService.getById(blogComment.getBlogId());
                noticeDTO.setFromId(userDTO.getId());
                noticeDTO.setFromNickname(userDTO.getNickName());
                noticeDTO.setFromIcon(userDTO.getIcon());
                noticeDTO.setType(3);
                noticeDTO.setContent(userDTO.getNickName() + "评论了你的博客");
                noticeDTO.setToId(blog.getUserId());
                noticeDTO.setBlogId(blog.getId());
                blog.setComments(blog.getComments() + 1);
                blogService.updateById(blog);
                stringRedisTemplate.delete(key);
//        chatClient.notice(noticeDTO);
                rabbitMQUtils.SendMessageNoticeQueue(noticeDTO);
            }
        });
        return extracted(blogComment, false);
    }

    @Override
    public List<CommentDTO> queryMyComments() {
        Long id = UserHolder.getUser().getId();
        String key = RedisConstants.MY_COMMENTS_KEY + id;
        String s = stringRedisTemplate.opsForValue().get(key);
        if(null != s){
            return JSONObject.parseArray(s, CommentDTO.class);
        }
        List<BlogComment> blogComments = query().eq("user_id", id).eq("delete_flag", 0).orderByDesc("create_time").list();
        List<CommentDTO> res = new LinkedList<>();
        for (BlogComment blogComment : blogComments) {
            res.add(extracted(blogComment, true));
        }
        stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(res), RedisConstants.EXPIRE_TIME);
        return res;
    }

    @Override
    public Boolean delComment(Long id) {
        UserDTO user = UserHolder.getUser();
        String key = RedisConstants.MY_COMMENTS_KEY + user.getId();
        UpdateWrapper<BlogComment> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("delete_flag", 1);
        updateWrapper.eq("id", id);
        boolean update = update(updateWrapper);
        stringRedisTemplate.delete(key);
        return update;
    }

    private CommentDTO extracted(BlogComment blogComment, boolean flag) {
        CommentDTO commentDTO = new CommentDTO();
        BeanUtil.copyProperties(blogComment, commentDTO);
        log.info("commentDTO为:" + commentDTO);
        Object object = userClient.queryUserById(blogComment.getUserId()).getData();
        log.info("查询结果:" + JSONObject.toJSONString(object));
        UserDTO userDTO = JSONObject.parseObject(JSONObject.toJSONString(object), UserDTO.class);
        log.info(userDTO.toString());
        commentDTO.setUser(userDTO);
        if(flag){
            Blog blog = blogService.getById(blogComment.getBlogId());
            commentDTO.setBlog(blog);
        }
        return commentDTO;
    }
}
