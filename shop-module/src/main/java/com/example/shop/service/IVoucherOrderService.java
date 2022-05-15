package com.example.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shop.dto.Result;
import com.example.shop.entity.VoucherOrder;

public interface IVoucherOrderService extends IService<VoucherOrder> {
    Result seckillVoucher(Long voucherId);

    Result findVoucherByUser(Long id);
}
