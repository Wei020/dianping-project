package com.example.feign.clients;

import com.example.feign.entity.Voucher;
import com.example.feign.entity.VoucherOrder;
import com.example.feign.fallback.ShopClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(value = "shopservice",fallbackFactory = ShopClientFallbackFactory.class)
public interface ShopClient {
    @GetMapping("/voucher-order/of/{id}")
    VoucherOrder findVoucherByUser(@PathVariable("id") Long id);
}
