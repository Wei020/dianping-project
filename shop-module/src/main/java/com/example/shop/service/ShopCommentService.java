package com.example.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shop.dto.ShopCommentDTO;
import com.example.shop.entity.ShopComment;

import java.util.List;

public interface ShopCommentService extends IService<ShopComment> {

    ShopCommentDTO addShopComment(ShopComment shopComment);

    List<ShopCommentDTO> queryShopComments(Long id, Boolean flag);

    Boolean deleteShopComment(ShopCommentDTO shopCommentDTO);

    ShopCommentDTO likeComment(Long id);
}
