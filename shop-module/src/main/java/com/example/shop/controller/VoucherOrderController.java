package com.example.shop.controller;


import com.example.shop.dto.Result;
import com.example.shop.service.IVoucherOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voucher-order")
public class VoucherOrderController {

    @Autowired
    private IVoucherOrderService voucherOrderService;

    @PostMapping("/seckill/{id}")
    public Result seckillVoucher(@PathVariable("id") Long voucherId) {
        return voucherOrderService.seckillVoucher(voucherId);
    }

    @GetMapping("/of/{id}")
    Result findVoucherByUser(@PathVariable("id") Long id){
        return voucherOrderService.findVoucherByUser(id);
    }

}
