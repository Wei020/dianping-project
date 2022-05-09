package com.dppojo.service.impl;


import com.dppojo.dto.Result;
import com.dppojo.entity.VoucherOrder;
import com.dppojo.mapper.VoucherOrderMapper;
import com.dppojo.service.ISeckillVoucherService;
import com.dppojo.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dppojo.utils.RedisIdWorker;
import com.dppojo.utils.UserHolder;
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
    //        一人一单判断，兜底方案，理论上不会有问题
        Long userId = order.getUserId();
//        创建锁对象
        RLock redisLock = redissonClient.getLock("lock:order:" + userId);
        boolean isLock = redisLock.tryLock();//空参，失败直接结束，参数：获取锁的最大等待时间、锁自动释放时间、时间单位
        if (!isLock) {
//            获取锁失败
            log.error("不允许重复下单");
            return;
        }
        try {
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
        } finally {
            redisLock.unlock();
        }
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
        try {
            SendMessageOrderQueue(voucherOrder);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok(orderId);
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
