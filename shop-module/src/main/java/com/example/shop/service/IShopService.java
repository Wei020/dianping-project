package com.example.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shop.dto.Result;
import com.example.shop.entity.Shop;

public interface IShopService extends IService<Shop> {
    Result queryById(Long id);

    Result update(Shop shop);

    Result queryShopByType(Integer typeId, Integer current, String sortBy, Double x, Double y);
}
