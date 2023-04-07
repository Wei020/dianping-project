package com.example.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.dto.Result;
import com.example.blog.dto.UserDTO;
import com.example.blog.entity.Follow;
import com.example.blog.mapper.FollowMapper;
import com.example.blog.service.FollowService;
import com.example.blog.utils.RedisConstants;
import com.example.blog.utils.UserHolder;
import com.example.feign.clients.UserClient;
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

    @Override
    public Result follow(Long followUserId, Boolean isFollow) {

        Long userId = UserHolder.getUser().getId();
        String key = "follows:" + userId;
//        判断到底是关注还是取关
        if(isFollow){
            Follow follow = new Follow();
            follow.setUserId(userId);
            follow.setFollowUserId(followUserId);
            boolean isSuccess = save(follow);
            if(isSuccess){
                stringRedisTemplate.opsForSet().add(key, followUserId.toString());
            }
        }else {
//            取关
            boolean isSuccess = remove(new QueryWrapper<Follow>()
                    .eq("user_id", userId)
                    .eq("follow_user_id", followUserId));
            if(isSuccess){
                stringRedisTemplate.opsForSet().remove(key, followUserId.toString());
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
    public List<UserDTO> queryFans() {
        Long id = UserHolder.getUser().getId();
        String key = RedisConstants.USER_FANS_KEY + id;
        String s = stringRedisTemplate.opsForValue().get(key);
        if(null != s){
            log.info(s);
            return JSONObject.parseArray(s, UserDTO.class);
        }
        List<Follow> follows = query().eq("follow_user_id", id).list();
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
        stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(res), RedisConstants.EXPIRE_TIME);
        return res;
    }
}
