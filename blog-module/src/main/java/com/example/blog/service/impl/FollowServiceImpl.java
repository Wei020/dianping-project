package com.example.blog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.dto.FollowDTO;
import com.example.blog.dto.Result;
import com.example.blog.dto.UserDTO;
import com.example.blog.entity.Follow;
import com.example.blog.mapper.FollowMapper;
import com.example.blog.service.FollowService;
import com.example.blog.utils.RabbitMQUtils;
import com.example.blog.utils.RedisConstants;
import com.example.blog.utils.SystemConstants;
import com.example.blog.utils.UserHolder;
import com.example.feign.clients.ChatClient;
import com.example.feign.clients.UserClient;
import com.example.feign.dto.NoticeDTO;
import com.example.feign.dto.UserListDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private RabbitMQUtils rabbitMQUtils;


    @Override
    public Result follow(Long followUserId, Boolean isFollow) {
        UserDTO userDTO = UserHolder.getUser();
        String key1 = "follows:" + userDTO.getId();
        String key2 = RedisConstants.USER_FANS_KEY + followUserId;
//        判断到底是关注还是取关
        if(isFollow){
            Follow follow = new Follow();
            follow.setUserId(userDTO.getId());
            follow.setFollowUserId(followUserId);
            boolean isSuccess = save(follow);
            if(isSuccess){
                stringRedisTemplate.opsForSet().add(key1, followUserId.toString());
                stringRedisTemplate.delete(key2);
                FollowDTO followDTO = new FollowDTO(userDTO.getId(), followUserId, true);
                rabbitMQUtils.SendMessageFollowQueue(followDTO);
//                userClient.followUser(userDTO.getId(), followUserId);
                NoticeDTO noticeDTO = new NoticeDTO();
                noticeDTO.setFromId(userDTO.getId());
                noticeDTO.setFromIcon(userDTO.getIcon());
                noticeDTO.setFromNickname(userDTO.getNickName());
                noticeDTO.setToId(followUserId);
                noticeDTO.setContent(userDTO.getNickName() + "关注了你");
                noticeDTO.setType(2);
//                chatClient.notice(noticeDTO);
                rabbitMQUtils.SendMessageNoticeQueue(noticeDTO);
            }
        }else {
//            取关
            boolean isSuccess = remove(new QueryWrapper<Follow>()
                    .eq("user_id", userDTO.getId())
                    .eq("follow_user_id", followUserId));
            if(isSuccess){
//                userClient.notFollowUser(userDTO.getId(), followUserId);
                stringRedisTemplate.opsForSet().remove(key1, followUserId.toString());
                stringRedisTemplate.delete(key2);
                FollowDTO followDTO = new FollowDTO(userDTO.getId(), followUserId, false);
                rabbitMQUtils.SendMessageFollowQueue(followDTO);
            }
        }
        return Result.ok();
    }

    @Override
    public Result isFollow(Long followUserId) {
        Long userId = UserHolder.getUser().getId();
//        查询是否关注
        Integer count = query().eq("user_id", userId)
                .eq("follow_user_id", followUserId).count();
        return Result.ok(count > 0);
    }

    @Override
    public Result followCommons(Long id) {
//        获取当前用户
        Long userId = UserHolder.getUser().getId();
        String key = "follows:" + userId;
        String key2 = "follows:" + id;
        Set<String> intersect = stringRedisTemplate.opsForSet().intersect(key, key2);
        if(intersect == null || intersect.isEmpty()){
            return Result.ok(Collections.emptyList());
        }
//        解析id
        List<Long> ids = intersect.stream().map(Long::valueOf).collect(Collectors.toList());
        UserListDTO userListDTO = new UserListDTO();
        userListDTO.setIds(ids);
        log.info("查询条件:" + userListDTO);
        Object object = userClient.queryUserList(userListDTO).getData();
        List<UserDTO> userDTOS = JSONObject.parseArray(JSONObject.toJSONString(object), UserDTO.class);
        log.info("转换结果:" + userDTOS);
        return Result.ok(userDTOS);
    }

    @Override
    public List<UserDTO> queryFans(Integer current) {
        Long id = UserHolder.getUser().getId();
        String key = RedisConstants.USER_FANS_KEY + id;
        String s = stringRedisTemplate.opsForValue().get(key);
        if(null != s && current == 1){
            log.info(s);
            return JSONObject.parseArray(s, UserDTO.class);
        }
        List<Follow> follows = query()
                .eq("follow_user_id", id)
                .page(new Page<Follow>(current, SystemConstants.MAX_PAGE_SIZE))
                .getRecords();
        if(follows == null || follows.size() == 0){
            return null;
        }
        List<UserDTO> res = new LinkedList<>();
        for (Follow follow : follows) {
            Object data = userClient.queryUserById(follow.getUserId()).getData();
            if(null == data){
                log.info("RPC调用失败！");
                continue;
            }
            UserDTO userDTO = JSONObject.parseObject(JSONObject.toJSONString(data), UserDTO.class);
            res.add(userDTO);
        }
        if(current == 1){
            stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(res), RedisConstants.EXPIRE_TIME);
        }
        return res;
    }
}
