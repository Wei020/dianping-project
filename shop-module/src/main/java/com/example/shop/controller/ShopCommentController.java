package com.example.shop.controller;

import com.example.shop.dto.Result;
import com.example.shop.dto.ShopCommentDTO;
import com.example.shop.entity.ShopComment;
import com.example.shop.service.ShopCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController()
@RequestMapping("/shop-comment")
public class ShopCommentController {

    @Autowired
    private ShopCommentService shopCommentService;

    @PostMapping("/add")
    public Result addShopComment(@RequestBody ShopComment shopComment){
        log.info("输入参数为:" + shopComment.toString());
        ShopCommentDTO shopCommentDTO = shopCommentService.addShopComment(shopComment);
        return Result.ok(shopCommentDTO);
    }

    @PostMapping("/{id}")
    public Result queryShopComments(@PathVariable("id") Long id){
        List<ShopCommentDTO> res = shopCommentService.queryShopComments(id, true);
        return Result.ok(res);
    }

    @PostMapping("/like/{id}")
    public Result likeComment(@PathVariable("id") Long id){
        ShopCommentDTO res = shopCommentService.likeComment(id);
        return Result.ok(res);
    }

    @PostMapping("/all/{id}")
    public Result queryAllShopComments(@PathVariable("id") Long id){
        List<ShopCommentDTO> res = shopCommentService.queryShopComments(id, false);
        return Result.ok(res);
    }

    @PostMapping("/delete")
    public Result deleteShopComment(@RequestBody ShopCommentDTO shopCommentDTO){
        Boolean res = shopCommentService.deleteShopComment(shopCommentDTO);
        return Result.ok(res);
    }
}
