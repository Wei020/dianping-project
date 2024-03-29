package com.example.shop.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.shop.dto.Result;
import com.example.shop.entity.Shop;
import com.example.shop.service.IShopService;
import com.example.shop.utils.SystemConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController()
@RequestMapping("/shop")
public class ShopController {

    @Resource
    public IShopService shopService;


    @GetMapping("/{id}")
    public Result queryShopById(@PathVariable("id") Long id) {
        return shopService.queryById(id);
    }


    @PostMapping
    public Result saveShop(@RequestBody Shop shop) {
        // 写入数据库
        log.info("接收数据:" + shop.toString());
        Long res = shopService.saveShop(shop);
//         返回店铺id
        return Result.ok(res);
    }


    @PutMapping
    public Result updateShop(@RequestBody Shop shop) {
        // 写入数据库
        return shopService.update(shop);
    }


    @GetMapping("/of/type")
    public Result queryShopByType(
            @RequestParam("typeId") Integer typeId,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "sortBy", defaultValue = "") String sortBy,
            @RequestParam(value = "condition", defaultValue = "") String condition
    ) {
        // 根据类型分页查询
//        Page<Shop> page = shopService.query()
//                .eq("type_id", typeId)
//                .page(new Page<>(current, SystemConstants.DEFAULT_PAGE_SIZE));
        // 返回数据
        return shopService.queryShopByType(typeId, current, sortBy, condition);
    }


    @GetMapping("/of/name")
    public Result queryShopByName(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "current", defaultValue = "1") Integer current
    ) {
        // 根据类型分页查询
        Page<Shop> page = shopService.query()
                .like(StrUtil.isNotBlank(name), "name", name)
                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 返回数据
        return Result.ok(page.getRecords());
    }


}
