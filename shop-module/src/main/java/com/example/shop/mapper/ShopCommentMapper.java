package com.example.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shop.entity.ShopComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShopCommentMapper extends BaseMapper<ShopComment> {

    Double queryAvgRateOfShop(@Param("shopId") Long shopId);
}
