package com.example.shop.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shop.dto.Result;
import com.example.shop.entity.Voucher;
import com.example.shop.entity.VoucherOrder;
import com.example.shop.mapper.VoucherOrderMapper;
import com.example.shop.service.ISeckillVoucherService;
import com.example.shop.service.IVoucherOrderService;
import com.example.shop.utils.RedisIdWorker;
import com.example.shop.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {

    @Autowired
    private ISeckillVoucherService seckillVoucherService;

    @Autowired
    private RedisIdWorker redisIdWorker;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "order.queue"),
            exchange = @Exchange(name = "order.direct",type = ExchangeTypes.DIRECT),
            key = "order.add"
    ))
    public void listenOrderQueue(VoucherOrder order) throws InterruptedException {
        createVoucherOrder(order);
    }

    private void createVoucherOrder(VoucherOrder order) {
    //        ????????????????????????????????????????????????????????????
        Long userId = order.getUserId();
//        ???????????????
        RLock redisLock = redissonClient.getLock("lock:order:" + userId);
        boolean isLock = redisLock.tryLock();//????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (!isLock) {
//            ???????????????
            log.error("?????????????????????");
            return;
        }
        try {
            Long voucherId = order.getVoucherId();
            int count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
            if(count > 0) {
                log.error("?????????????????????");
                return;
            }
//        ????????????????????????
            boolean success = seckillVoucherService.update()
                    .setSql("stock = stock - 1")
                    .eq("voucher_id", voucherId)
                    .gt("stock",0)//??????0????????????
                    .update();
            if(!success){
                log.error("????????????");
                return;
            }
            save(order);
        } finally {
            redisLock.unlock();
        }
    }

    @Override
    public Result seckillVoucher(Long voucherId) {
        Long userId = UserHolder.getUser().getId();
        long orderId = redisIdWorker.nextId("order");
//        1?????????lua??????,????????????
        Long result = stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                voucherId.toString(),
                userId.toString()
        );
        int r = result.intValue();
        if(r != 0){
            return Result.fail(r == 1 ? "???????????????" : "????????????????????????");
        }
        VoucherOrder voucherOrder = new VoucherOrder();
        voucherOrder.setId(orderId);
        voucherOrder.setUserId(userId);
        voucherOrder.setVoucherId(voucherId);
        try {
            SendMessageOrderQueue(voucherOrder);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok(orderId);
    }

    @Override
    public VoucherOrder findVoucherByUser(Long id) {
        VoucherOrder voucherOrder = query().eq("user_id", id).one();
        return voucherOrder;
    }

    public void SendMessageOrderQueue(VoucherOrder voucherOrder) throws InterruptedException {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
//        ????????????
        correlationData.getFuture().addCallback(confirm -> {
            if(confirm.isAck()){
                // 3.1.ack???????????????
                log.debug("??????????????????????????????, ID:{}", correlationData.getId());
            }else{
                // 3.2.nack???????????????
                log.error("??????????????????????????????, ID:{}, ??????{}",correlationData.getId(), confirm.getReason());
//                ????????????
                rabbitTemplate.convertAndSend("order.direct", "order.add", voucherOrder,correlationData);
            }
        }, throwable ->{
            log.error("??????????????????, ID:{}, ??????{}",correlationData.getId(),throwable.getMessage());
            rabbitTemplate.convertAndSend("order.direct", "order.add", voucherOrder,correlationData);
        }) ;
        rabbitTemplate.convertAndSend("order.direct","order.add", voucherOrder,correlationData);
    }
}
