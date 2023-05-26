package com.example.shop.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.feign.clients.UserClient;
import com.example.feign.entity.UserInfo;
import com.example.shop.dto.Result;
import com.example.shop.dto.UserDTO;
import com.example.shop.entity.Shop;
import com.example.shop.mapper.ShopMapper;
import com.example.shop.service.IShopService;
import com.example.shop.utils.CacheClient;
import com.example.shop.utils.RedisConstants;
import com.example.shop.utils.SystemConstants;
import com.example.shop.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.example.shop.utils.RedisConstants.*;

@Slf4j
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CacheClient cacheClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ShopMapper shopMapper;

    @Override
    public Result queryById(Long id) {
//        缓存穿透
        Shop shop = cacheClient
                .queryWithPassThrough(CACHE_SHOP_KEY, id, Shop.class, this::getById, CACHE_SHOP_TTL, TimeUnit.MINUTES);
//        针对热点key处理方法如下
//        互斥锁解决缓存击穿
//        Shop shop = queryWithMutex(id);
//        逻辑过期解决缓存击穿
//        Shop shop = queryWithLogicalExpire(id);
        if (shop == null) {
            return Result.fail("店铺不存在！");
        }
        return Result.ok(shop);
    }

    @Override
    @Transactional
    public Result update(Shop shop) {
        Long id = shop.getId();
        if(id == null){
            return Result.fail("店铺id不能为空");
        }
//        1、先更新数据库
        updateById(shop);
//        2、删除缓存
        stringRedisTemplate.delete(CACHE_SHOP_KEY + id);
        return Result.ok();
    }

    @Override
    public Result queryShopByType(Integer typeId, Integer current, String sortBy, String condition) {
        log.info("排序方式为:" + sortBy);
        UserDTO user = UserHolder.getUser();
        boolean isLogin = false;
        UserInfo userInfo = null;
        if(user != null){
            Object data = userClient.queryUserInfoById(user.getId()).getData();
            userInfo = JSONObject.parseObject(JSONObject.toJSONString(data), UserInfo.class);
            if(userInfo != null && userInfo.getCity() != null){
                isLogin = true;
            }
        }
        log.info("islogin为:" + isLogin);
        QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id", typeId);
        if(isLogin && sortBy.equals("city")){
            queryWrapper.eq("area", userInfo.getCity());
            queryWrapper.orderByDesc("score");
        }
        queryWrapper.like(!condition.isEmpty(), "name", condition);
        queryWrapper.orderBy(!sortBy.isEmpty() && !sortBy.equals("city"), false, sortBy);
        Page<Shop> page = new Page<>(current, SystemConstants.DEFAULT_PAGE_SIZE);
        List<Shop> records = shopMapper.selectPage(page, queryWrapper).getRecords();
//        if(page.getRecords().isEmpty())
//            return Result.ok();
        if (records.isEmpty())
            return Result.ok();
        return Result.ok(records);
    }

    @Override
    public Long saveShop(Shop shop) {
        if(shop.getId() != null){
            String key = CACHE_SHOP_KEY + shop.getId();
            shop.setUpdateTime(LocalDateTime.now());
            stringRedisTemplate.delete(key);
            updateById(shop);
            return shop.getId();
        }
        shop.setComments(0);
        shop.setSold(0);
        shop.setScore(30);
        shop.setCreateId(UserHolder.getUser().getId());
        shop.setAvgPrice(80L);
        save(shop);
        return shop.getId();
    }

}
