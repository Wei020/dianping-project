package com.example.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.dto.CommentDTO;
import com.example.blog.dto.UserDTO;
import com.example.blog.entity.Blog;
import com.example.blog.entity.BlogComment;
import com.example.blog.mapper.BlogCommentsMapper;
import com.example.blog.service.BlogCommentsService;
import com.example.blog.service.BlogService;
import com.example.blog.utils.*;
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
import java.util.Objects;
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
    private RabbitMQUtils rabbitMQUtils;

    @Override
    public List<CommentDTO> queryByBlogId(Long id, Boolean flag) {
        List<BlogComment> blogComments = query().eq("blog_id", id)
                .eq("parent_id", 0)
                .eq("delete_flag", 0)
                .orderByDesc("responds", "create_time")
                .last(flag, "limit 0, 5")
                .list();
        List<CommentDTO> res = new LinkedList<>();
        for (BlogComment blogComment : blogComments) {
            CommentDTO commentDTO = extracted(blogComment, false);
            if(blogComment.getResponds() != 0){
                List<BlogComment> comments = query().eq("parent_id", blogComment.getId())
                        .eq("delete_flag", 0).list();
                List<CommentDTO> responds = new LinkedList<>();
                for (BlogComment comment : comments) {
                    responds.add(extracted(comment, false));
                }
                commentDTO.setRespondLists(responds);
            }
            res.add(commentDTO);
        }
        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommentDTO addComment(CommentDTO commentDTO) {
        log.info("评论内容:" + commentDTO.toString());
        BlogComment blogComment = BeanUtil.copyProperties(commentDTO, BlogComment.class);
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
                if(!Objects.equals(noticeDTO.getToId(), userDTO.getId())){
                    rabbitMQUtils.SendMessageNoticeQueue(noticeDTO);
                }
                if(blogComment.getParentId() != 0){
                    update().setSql("responds = responds + 1").eq("id", blogComment.getParentId()).update();
                    NoticeDTO noticeDTO1 = BeanUtil.copyProperties(noticeDTO, NoticeDTO.class);
                    noticeDTO1.setType(4);
                    noticeDTO1.setContent(userDTO.getNickName() + "回复了你的评论");
                    noticeDTO1.setToId(commentDTO.getAnswerUser().getId());
                    rabbitMQUtils.SendMessageNoticeQueue(noticeDTO1);
                }
                blogService.update().setSql("sort = sort + 3").eq("id", blog.getId()).update();
            }
        });
        return extracted(blogComment, false);
    }

    @Override
    public List<CommentDTO> queryMyComments(Integer current) {
        Long id = UserHolder.getUser().getId();
        String key = RedisConstants.MY_COMMENTS_KEY + id;
        String s = stringRedisTemplate.opsForValue().get(key);
        if(null != s && current == 1){
            return JSONObject.parseArray(s, CommentDTO.class);
        }
        List<BlogComment> blogComments = query().eq("user_id", id)
                .eq("delete_flag", 0)
                .orderByDesc("create_time")
                .page(new Page<BlogComment>(current, SystemConstants.My_PAGE_SIZE))
                .getRecords();
        if(blogComments == null || blogComments.size() == 0){
            return null;
        }
        List<CommentDTO> res = new LinkedList<>();
        for (BlogComment blogComment : blogComments) {
            res.add(extracted(blogComment, true));
        }
        if(current == 1){
            stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(res), RedisConstants.EXPIRE_TIME);
        }
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
        ThreadPoolExecutor poolExecutor = ThreadPool.poolExecutor;
        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                BlogComment blogComment = getById(id);
                blogService.update().setSql("sort = sort - 3").eq("id", blogComment.getBlogId()).update();
                if(blogComment.getParentId() != 0){
                    update().setSql("responds = responds - 1").eq("id", blogComment.getParentId()).update();
                }
            }
        });
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
        if(blogComment.getAnswerId() != null && commentDTO.getAnswerUser() == null){
            BlogComment blogComment1 = getById(blogComment.getAnswerId());
            Object object1 = userClient.queryUserById(blogComment1.getUserId()).getData();
            log.info("查询结果:" + JSONObject.toJSONString(object1));
            UserDTO userDTO1 = JSONObject.parseObject(JSONObject.toJSONString(object1), UserDTO.class);
            commentDTO.setAnswerUser(userDTO1);
        }
        if(flag){
            Blog blog = blogService.getById(blogComment.getBlogId());
            commentDTO.setBlog(blog);
        }
        return commentDTO;
    }
}
