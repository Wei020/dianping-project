package com.dppojo.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.dppojo.dto.Result;
import com.dppojo.entity.ShopType;
import com.dppojo.mapper.ShopTypeMapper;
import com.dppojo.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dppojo.utils.RedisConstants.CACHE_SHOP_LIST;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
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
