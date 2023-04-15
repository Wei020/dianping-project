package com.example.shop.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shop.dto.Result;
import com.example.shop.dto.VoucherDTO;
import com.example.shop.entity.SeckillVoucher;
import com.example.shop.entity.Shop;
import com.example.shop.entity.Voucher;
import com.example.shop.entity.VoucherOrder;
import com.example.shop.mapper.VoucherMapper;
import com.example.shop.service.ISeckillVoucherService;
import com.example.shop.service.IShopService;
import com.example.shop.service.IVoucherOrderService;
import com.example.shop.service.IVoucherService;
import com.example.shop.utils.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

import static com.example.shop.utils.RedisConstants.SECKILL_STOCK_KEY;


@Service
public class VoucherServiceImpl extends ServiceImpl<VoucherMapper, Voucher> implements IVoucherService {

    @Resource
    private ISeckillVoucherService seckillVoucherService;

    @Autowired
    private IVoucherOrderService voucherOrderService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IShopService shopService;

    @Override
    public Result queryVoucherOfShop(Long shopId) {
        // 查询优惠券信息
        List<Voucher> vouchers = getBaseMapper().queryVoucherOfShop(shopId);
        // 返回结果
        return Result.ok(vouchers);
    }

    @Override
    @Transactional
    public void addSeckillVoucher(Voucher voucher) {
        // 保存优惠券
        save(voucher);
        // 保存秒杀信息
        SeckillVoucher seckillVoucher = new SeckillVoucher();
        seckillVoucher.setVoucherId(voucher.getId());
        seckillVoucher.setStock(voucher.getStock());
        seckillVoucher.setBeginTime(voucher.getBeginTime());
        seckillVoucher.setEndTime(voucher.getEndTime());
        seckillVoucherService.save(seckillVoucher);
//        写入redis
        stringRedisTemplate.opsForValue().set(SECKILL_STOCK_KEY + voucher.getId(), voucher.getStock().toString());
    }

    @Override
    public List<VoucherDTO> findVoucherByUser(Long id) {
        String key = RedisConstants.USER_VOUCHER_LIST + id;
        String s = stringRedisTemplate.opsForValue().get(key);
        if(null != s){
            List<VoucherDTO> res = JSONObject.parseArray(s, VoucherDTO.class);
            return res;
        }
        List<VoucherOrder> orders = voucherOrderService.query().eq("user_id", id).orderByDesc("create_time").list();
        List<VoucherDTO> res = new LinkedList<>();
        for (VoucherOrder order : orders) {
            Voucher voucher = getById(order.getVoucherId());
            VoucherDTO voucherDTO = new VoucherDTO();
            BeanUtil.copyProperties(voucher, voucherDTO);
            Shop shop = shopService.getById(voucher.getShopId());
            voucherDTO.setShopName(shop.getName());
            if (voucher.getType() == 1){
                SeckillVoucher seckillVoucher = seckillVoucherService.getById(voucher.getId());
                voucherDTO.setBeginTime(seckillVoucher.getBeginTime());
                voucherDTO.setEndTime(seckillVoucher.getEndTime());
            }
            res.add(voucherDTO);
        }
        stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(res));
        return res;
    }
}
