package com.example.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shop.dto.Result;
import com.example.shop.dto.VoucherDTO;
import com.example.shop.entity.Voucher;

import java.util.List;


public interface IVoucherService extends IService<Voucher> {

    Result queryVoucherOfShop(Long shopId);

    List<VoucherDTO> findVoucherByUser(Long id);

    boolean addVoucher(VoucherDTO voucherDTO);
}
