package com.example.shop.controller;

import com.example.shop.dto.Result;
import com.example.shop.dto.VoucherDTO;
import com.example.shop.entity.Voucher;
import com.example.shop.service.IVoucherService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/voucher")
public class VoucherController {

    @Resource
    private IVoucherService voucherService;

    @PostMapping
    public Result addVoucher(@RequestBody Voucher voucher) {
        voucherService.save(voucher);
        return Result.ok(voucher.getId());
    }

    @PostMapping("/seckill")
    public Result addSeckillVoucher(@RequestBody Voucher voucher) {
        voucherService.addSeckillVoucher(voucher);
        return Result.ok(voucher.getId());
    }


    @GetMapping("/list/{shopId}")
    public Result queryVoucherOfShop(@PathVariable("shopId") Long shopId) {
       return voucherService.queryVoucherOfShop(shopId);
    }

    @PostMapping("/of/{id}")
    public Result findVoucherByUser(@PathVariable("id") Long id){
        List<VoucherDTO> res= voucherService.findVoucherByUser(id);
        return Result.ok(res);
    }
}
