package com.example.feign.clients;


import com.example.feign.dto.Result;
import com.example.feign.entity.Voucher;
import com.example.feign.fallback.ShopClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@ResponseBody
@FeignClient(value = "shopservice",fallbackFactory = ShopClientFallbackFactory.class)
public interface ShopClient {
    @GetMapping("/voucher-order/of/{id}")
    List<Voucher> findVoucherByUser(@PathVariable("id") Long id);
}
