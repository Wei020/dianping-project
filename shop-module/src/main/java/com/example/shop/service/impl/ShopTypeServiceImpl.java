package com.example.shop.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shop.dto.Result;
import com.example.shop.entity.ShopType;
import com.example.shop.mapper.ShopTypeMapper;
import com.example.shop.service.IShopTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.shop.utils.RedisConstants.CACHE_SHOP_LIST;

@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryType() {
        String key = CACHE_SHOP_LIST;
        String shopListJson = stringRedisTemplate.opsForValue().get(key);
        if(StrUtil.isNotBlank(shopListJson)){
            List<ShopType> shopTypeList = JSONUtil.toList(shopListJson, ShopType.class);
            return Result.ok(shopTypeList);
        }
        List<ShopType> typeList = query().orderByAsc("sort").list();
        stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(typeList));
        return Result.ok(typeList);
    }
}
