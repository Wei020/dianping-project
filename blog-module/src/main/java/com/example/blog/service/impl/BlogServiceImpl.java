package com.example.blog.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.dto.Result;
import com.example.blog.dto.ScrollResult;
import com.example.blog.dto.UserDTO;
import com.example.blog.entity.Blog;
import com.example.blog.entity.Follow;
import com.example.blog.mapper.BlogMapper;
import com.example.blog.service.BlogService;
import com.example.blog.service.FollowService;
import com.example.blog.utils.SystemConstants;
import com.example.blog.utils.UserHolder;
import com.example.feign.clients.UserClient;
import com.example.feign.dto.UserListDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.blog.utils.RedisConstants.BLOG_LIKED_KEY;
import static com.example.blog.utils.RedisConstants.FEED_KEY;

@Slf4j
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    @Autowired
    private UserClient userClient;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private FollowService followService;

    @Override
    public Result queryBlogById(Long id) {
//        查询blog
        Blog blog = getById(id);
        if(blog == null)
            return Result.fail("笔记不存在！");
//        查询blog有关的用户
        queryBlogUser(blog);
        isBlogLiked(blog);
        return Result.ok(blog);
    }

    private void isBlogLiked(Blog blog) {
        UserDTO user = UserHolder.getUser();
        if(user == null)
//            用户未登录，无需查询是否点赞
            return;
        Long userId = user.getId();
//        判断当前用户是否已经点赞
        String key = BLOG_LIKED_KEY + blog.getId();
        Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
        blog.setIsLike(BooleanUtil.isTrue(score != null));
    }

    private void queryBlogUser(Blog blog) {
        Long userId = blog.getUserId();
        Object object = userClient.queryUserById(userId).getData();
        log.info("查询结果:" + JSONObject.toJSONString(object));
        UserDTO userDTO = JSONObject.parseObject(JSONObject.toJSONString(object), UserDTO.class);
        blog.setName(userDTO.getNickName());
        blog.setIcon(userDTO.getIcon());
    }

    @Override
    public Result queryHotBlog(Integer current) {
        // 根据用户查询
        Page<Blog> page = query()
                .eq("delete_flag", 0)
                .orderByDesc("liked")
                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 获取当前页数据
        List<Blog> records = page.getRecords();
        // 查询用户
        records.forEach(blog -> {
            this.queryBlogUser(blog);
            this.isBlogLiked(blog);
        });
        if(records.isEmpty())
            return Result.ok();
        return Result.ok(records);
    }

    @Override
    public Result likeBlog(Long id) {
//        获取登录用户
        Long userId = UserHolder.getUser().getId();
//        判断当前用户是否已经点赞
        String key = BLOG_LIKED_KEY + id;
        Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
        Blog blog = getById(id);
        if(score == null){
            //        未点赞，加1
            boolean isSuccess = update().setSql("liked = liked + 1").eq("id", id).update();
            if(isSuccess){
                stringRedisTemplate.opsForZSet().add(key, userId.toString(), System.currentTimeMillis());
                blog.setIsLike(true);
                blog.setLiked(blog.getLiked() + 1);
            }
        }else{
            //        点赞，减1
            boolean isSuccess = update().setSql("liked = liked - 1").eq("id", id).update();
            if(isSuccess){
                stringRedisTemplate.opsForZSet().remove(key, userId.toString());
                blog.setIsLike(false);
                blog.setLiked(blog.getLiked() - 1);
            }
        }
        return Result.ok(blog);
    }

//    查询点赞top5
    @Override
    public Result queryBlogLikes(Long id) {
        String key = BLOG_LIKED_KEY + id;
        Set<String> top5 = stringRedisTemplate.opsForZSet().range(key,0,4);
        if(top5 == null || top5.isEmpty())
            return Result.ok(Collections.emptyList());
        List<Long> ids = top5.stream().map(Long::valueOf).collect(Collectors.toList());
        String idStr = StrUtil.join(",", ids);
        UserListDTO userListDTO = new UserListDTO();
        userListDTO.setIds(ids);
        userListDTO.setIdStr(idStr);
        log.info("查询条件:" + userListDTO);
        Object object = userClient.queryUserList(userListDTO).getData();
        log.info("远程获取结果:" + JSONObject.toJSONString(object));
        List<UserDTO> userDTOS = JSONObject.parseArray(JSONObject.toJSONString(object), UserDTO.class);
        return Result.ok(userDTOS);
    }

    @Override
    public Result saveBlog(Blog blog) {
        if(blog.getId() != null){
            blog.setUpdateTime(LocalDateTime.now());
            return Result.ok(updateById(blog));
        }
        UserDTO user = UserHolder.getUser();
        blog.setUserId(user.getId());
        blog.setComments(0);
        blog.setLiked(0);
        // 保存探店博文
        boolean isSuccess = save(blog);
//        查询笔记作者的所有粉丝
        if(!isSuccess){
            return Result.fail("新增笔记失败！");
        }
        List<Follow> follows = followService.query().eq("follow_user_id", user.getId()).list();
        for (Follow follow : follows) {
            Long userId = follow.getUserId();
            String key = FEED_KEY + userId;
            stringRedisTemplate.opsForZSet().add(key, blog.getId().toString(), System.currentTimeMillis());
        }
        return Result.ok();
    }

//    滚动分页查询
    @Override
    public Result queryBlogOfFollow(Long max, Integer offset) {
        Long userId = UserHolder.getUser().getId();
        String key = FEED_KEY + userId;
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet()
                .reverseRangeByScoreWithScores(key, 0, max, offset, 3);
//        非空判断
        if(typedTuples == null || typedTuples.isEmpty())
            return Result.ok();
//        解析数据
        List<Long> ids = new ArrayList<>(typedTuples.size());
        long minTime = 0;
        int os = 1;
//        os:计算本次查询score最小值相同个数作为偏移量
        for (ZSetOperations.TypedTuple<String> tuple : typedTuples) {
            ids.add(Long.valueOf(tuple.getValue()));
            long time = tuple.getScore().longValue();
            if(time == minTime)
                os++;
            else {
                os = 1;
                minTime = time;
            }
        }
//        根据id查询blog
        String idStr = StrUtil.join(",", ids);
        List<Blog> blogs = query().in("id", ids).last("order by field(id, " + idStr + ")").list();
        for (Blog blog : blogs) {
            queryBlogUser(blog);
            isBlogLiked(blog);
        }
//        封装返回
        ScrollResult r = new ScrollResult();
        r.setList(blogs);
        r.setMinTime(minTime);
        r.setOffset(os);
        return Result.ok(r);
    }

    @Override
    public Result queryMyBlog(Integer current) {
        Long id = UserHolder.getUser().getId();
        Page<Blog> page = query()
                .orderByDesc("create_time")
                .eq("user_id", id)
                .page(new Page<>(current, SystemConstants.My_PAGE_SIZE));
        // 获取当前页数据
        List<Blog> records = page.getRecords();
        // 查询用户
        records.forEach(blog -> {
            this.queryBlogUser(blog);
            this.isBlogLiked(blog);
        });
        if(records.isEmpty())
            return Result.ok();
        return Result.ok(records);
    }

    @Override
    public Boolean deleteBlog(Long id) {
        UpdateWrapper<Blog> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("delete_flag", 0);
        updateWrapper.eq("id", id);
        return update(updateWrapper);
    }

    @Override
    public List<Blog> queryBlogs(String condition) {
        List<Blog> blogs = query().eq("delete_flag", 0).like("title", condition).list();
        return blogs;
    }
}
