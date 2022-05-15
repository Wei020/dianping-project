package com.example.feign.fallback;

import com.example.feign.clients.ShopClient;
import com.example.feign.dto.Result;
import com.example.feign.entity.Voucher;
import com.example.feign.entity.VoucherOrder;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ShopClientFallbackFactory implements FallbackFactory<ShopClient> {
    @Override
    public ShopClient create(Throwable throwable) {
        return new ShopClient() {
            @Override
            public VoucherOrder findVoucherByUser(Long id) {
                log.error("查询异常",throwable);
                return null;
            }
        };
    }
}
