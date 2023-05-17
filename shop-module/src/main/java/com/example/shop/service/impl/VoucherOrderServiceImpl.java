package com.example.shop.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shop.dto.Result;
import com.example.shop.entity.Shop;
import com.example.shop.entity.Voucher;
import com.example.shop.entity.VoucherOrder;
import com.example.shop.mapper.VoucherOrderMapper;
import com.example.shop.service.ISeckillVoucherService;
import com.example.shop.service.IShopService;
import com.example.shop.service.IVoucherOrderService;
import com.example.shop.service.IVoucherService;
import com.example.shop.utils.RedisConstants;
import com.example.shop.utils.RedisIdWorker;
import com.example.shop.utils.ThreadPool;
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

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    private IVoucherService voucherService;

    @Autowired
    private IShopService shopService;

//    @Autowired
//    private RedissonClient redissonClient;

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
    //        一人一单判断，兜底方案，理论上不会有问题
        Long userId = order.getUserId();
////        创建锁对象
//        RLock redisLock = redissonClient.getLock("lock:order:" + userId);
//        boolean isLock = redisLock.tryLock();//空参，失败直接结束，参数：获取锁的最大等待时间、锁自动释放时间、时间单位
//        if (!isLock) {
////            获取锁失败
//            log.error("不允许重复下单");
//            return;
//        }
//        try {
        Long voucherId = order.getVoucherId();
        int count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
        if(count > 0) {
            log.error("不允许重复下单");
            return;
        }
//        扣减库存，乐观锁
        boolean success = seckillVoucherService.update()
                .setSql("stock = stock - 1")
                .eq("voucher_id", voucherId)
                .gt("stock",0)//大于0就能执行
                .update();
        if(!success){
            log.error("库存不足");
            return;
        }
        save(order);
        Voucher voucher = voucherService.getById(voucherId);
        shopService.update().setSql("sold = sold + 1")
                .eq("id", voucher.getShopId()).update();
        String key = RedisConstants.USER_VOUCHER_LIST + userId;
        stringRedisTemplate.delete(key);
//        } finally {
//            redisLock.unlock();
//        }
    }

    @Override
    public Result seckillVoucher(Long voucherId) {
        Long userId = UserHolder.getUser().getId();
        long orderId = redisIdWorker.nextId("order");
//        1、执行lua脚本,判断结果
        Long result = stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                voucherId.toString(),
                userId.toString()
        );
        int r = result.intValue();
        if(r != 0){
            return Result.fail(r == 1 ? "库存不足！" : "不允许重复下单！");
        }
        VoucherOrder voucherOrder = new VoucherOrder();
        voucherOrder.setId(orderId);
        voucherOrder.setUserId(userId);
        voucherOrder.setVoucherId(voucherId);
        //            SendMessageOrderQueue(voucherOrder);
        ThreadPoolExecutor poolExecutor = ThreadPool.poolExecutor;
        poolExecutor.execute(() -> createVoucherOrder(voucherOrder));
        return Result.ok(orderId);
    }

    @Override
    public VoucherOrder findVoucherByUser(Long id) {
        VoucherOrder voucherOrder = query().eq("user_id", id).one();
        return voucherOrder;
    }

    @Override
    public Boolean killVoucher(Long voucherId) {
        Long userId = UserHolder.getUser().getId();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String day = simpleDateFormat.format(date);
        String dayKey = RedisConstants.VOUCHER_DAY_USERS + voucherId + ":" + day;
        if(Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(dayKey, userId.toString()))) {
            return false;
        }
        long orderId = redisIdWorker.nextId("order");
        VoucherOrder voucherOrder = new VoucherOrder();
        voucherOrder.setId(orderId);
        voucherOrder.setUserId(userId);
        voucherOrder.setVoucherId(voucherId);
        String key = RedisConstants.USER_VOUCHER_LIST + userId;
        boolean res = save(voucherOrder);
        if (res){
            stringRedisTemplate.delete(key);
            if(Boolean.FALSE.equals(stringRedisTemplate.hasKey(dayKey))){
                stringRedisTemplate.opsForSet().add(dayKey, userId.toString());
                stringRedisTemplate.expire(dayKey, 24L, TimeUnit.HOURS);
            }else {
                stringRedisTemplate.opsForSet().add(dayKey, userId.toString());
            }
            ThreadPoolExecutor poolExecutor = ThreadPool.poolExecutor;
            poolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    Voucher voucher = voucherService.getById(voucherId);
                    shopService.update().setSql("sold = sold + 1")
                            .eq("id", voucher.getShopId())
                            .update();
                }
            });
        }
        return res;
    }

    public void SendMessageOrderQueue(VoucherOrder voucherOrder) throws InterruptedException {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
//        异步回调
        correlationData.getFuture().addCallback(confirm -> {
            if(confirm.isAck()){
                // 3.1.ack，消息成功
                log.debug("消息成功发送到交换机, ID:{}", correlationData.getId());
            }else{
                // 3.2.nack，消息失败
                log.error("消息发送到交换机失败, ID:{}, 原因{}",correlationData.getId(), confirm.getReason());
//                重发消息
                rabbitTemplate.convertAndSend("order.direct", "order.add", voucherOrder,correlationData);
            }
        }, throwable ->{
            log.error("消息发送异常, ID:{}, 原因{}",correlationData.getId(),throwable.getMessage());
            rabbitTemplate.convertAndSend("order.direct", "order.add", voucherOrder,correlationData);
        }) ;
        rabbitTemplate.convertAndSend("order.direct","order.add", voucherOrder,correlationData);
    }
}
