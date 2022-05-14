package com.example.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shop.dto.Result;
import com.example.shop.entity.Voucher;


public interface IVoucherService extends IService<Voucher> {

    Result queryVoucherOfShop(Long shopId);

    void addSeckillVoucher(Voucher voucher);
}
