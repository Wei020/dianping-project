package com.example.shop.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.feign.clients.UserClient;
import com.example.shop.dto.ShopCommentDTO;
import com.example.shop.dto.UserDTO;
import com.example.shop.entity.ShopComment;
import com.example.shop.mapper.ShopCommentMapper;
import com.example.shop.service.IShopService;
import com.example.shop.service.ShopCommentService;
import com.example.shop.utils.RedisConstants;
import com.example.shop.utils.ThreadPool;
import com.example.shop.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;


@Service
@Slf4j
public class ShopCommentServiceImpl extends ServiceImpl<ShopCommentMapper, ShopComment> implements ShopCommentService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private IShopService shopService;

    @Autowired
    private ShopCommentMapper shopCommentMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public ShopCommentDTO addShopComment(ShopComment shopComment) {
        shopComment.setDeleteFlag(0);
        shopComment.setLiked(0);
//        shopComment.setRate(shopComment.getRate() * 10);
        save(shopComment);
        ThreadPoolExecutor poolExecutor = ThreadPool.poolExecutor;
        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                String key = RedisConstants.CACHE_SHOP_KEY + shopComment.getShopId();
                Double rate = shopCommentMapper.queryAvgRateOfShop(shopComment.getShopId());
                log.info("平均分:" + rate);
                boolean update = shopService.update().eq("id", shopComment.getShopId()).set("score", rate)
                        .setSql("comments = comments + 1").update();
                if(update){
                    stringRedisTemplate.delete(key);
                }
            }
        });
        return getShopCommentDTO(shopComment);
    }

    private ShopCommentDTO getShopCommentDTO(ShopComment shopComment) {
        ShopCommentDTO shopCommentDTO = BeanUtil.copyProperties(shopComment, ShopCommentDTO.class);
//        shopCommentDTO.setRate(shopComment.getRate() / 10);
        if(shopCommentDTO.getImages().isEmpty()){
            shopCommentDTO.setImages(null);
        }
        Long userId = shopComment.getUserId();
        if(userId.equals(UserHolder.getUser().getId())){
            shopCommentDTO.setUser(UserHolder.getUser());
        }else {
            Object data = userClient.queryUserById(userId).getData();
            UserDTO userDTO = JSONObject.parseObject(JSONObject.toJSONString(data), UserDTO.class);
            shopCommentDTO.setUser(userDTO);
        }
        return shopCommentDTO;
    }

    @Override
    public List<ShopCommentDTO> queryShopComments(Long id, Boolean flag) {
        List<ShopComment> shopComments = query().eq("shop_id", id)
                .eq("delete_flag", 0)
                .orderByDesc("liked", "create_time")
                .last(flag, "limit 0, 5")
                .list();
        List<ShopCommentDTO> res = new LinkedList<>();
        for (ShopComment shopComment : shopComments) {
            res.add(getShopCommentDTO(shopComment));
        }
        return res;
    }

    @Override
    public Boolean deleteShopComment(ShopCommentDTO shopCommentDTO) {
        UpdateWrapper<ShopComment> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("delete_flag", 1);
        updateWrapper.eq("id", shopCommentDTO.getId());
        boolean update = update(updateWrapper);
        ThreadPoolExecutor poolExecutor = ThreadPool.poolExecutor;
        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                String key = RedisConstants.Comment_LIKED_KEY + shopCommentDTO.getId();
                stringRedisTemplate.delete(key);
                shopService.update().setSql("comments = comments - 1").eq("id", shopCommentDTO.getShopId()).update();
            }
        });
        return update;
    }

    @Override
    public ShopCommentDTO likeComment(Long id) {
        //        获取登录用户
        Long userId = UserHolder.getUser().getId();
//        判断当前用户是否已经点赞
        String key = RedisConstants.Comment_LIKED_KEY + id;
        Boolean isMem = stringRedisTemplate.opsForSet().isMember(key, userId.toString());
        ShopComment shopComment = getById(id);
        ShopCommentDTO shopCommentDTO = BeanUtil.copyProperties(shopComment, ShopCommentDTO.class);
        if(Boolean.FALSE.equals(isMem)){
            //        未点赞，加1
            boolean isSuccess = update().setSql("liked = liked + 1").eq("id", id).update();
            if(isSuccess){
                stringRedisTemplate.opsForSet().add(key, userId.toString());
                shopCommentDTO.setIsLike(true);
                shopCommentDTO.setLiked(shopCommentDTO.getLiked() + 1);
            }
        }else{
            //        点赞，减1
            boolean isSuccess = update().setSql("liked = liked - 1").eq("id", id).update();
            if(isSuccess){
                stringRedisTemplate.opsForSet().remove(key, userId.toString());
                shopCommentDTO.setIsLike(false);
                shopCommentDTO.setLiked(shopCommentDTO.getLiked() - 1);
            }
        }
        return shopCommentDTO;
    }
}
