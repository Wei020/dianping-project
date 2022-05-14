package com.example.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shop.dto.Result;
import com.example.shop.entity.ShopType;


public interface IShopTypeService extends IService<ShopType> {
    Result queryType();
}
