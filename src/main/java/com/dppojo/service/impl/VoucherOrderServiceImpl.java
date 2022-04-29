package com.dppojo.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
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

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    private static final ExecutorService SECKILL_ORDER_EXECUTOR = Executors.newSingleThreadExecutor();

    @PostConstruct//在当前类初始化之后执行
    private void init(){
        SECKILL_ORDER_EXECUTOR.submit(new VoucherOrderHandler());
    }

    /*private ArrayBlockingQueue<VoucherOrder> orderTasks = new ArrayBlockingQueue<>(1024 * 1024);

    private class VoucherOrderHandler implements Runnable{

        @Override
        public void run() {
            while (true){
                try {
//                获取队列中额订单信息
                    VoucherOrder order = orderTasks.take();
//                    创建订单
                    createVoucherOrder(order);
                } catch (InterruptedException e) {
                    log.error("处理订单异常", e);
                }
            }
        }
    }*/

    private class VoucherOrderHandler implements Runnable{

        @Override
        public void run() {
            String queueName = "stream.orders";
            while (true){
                try {
//                获取消息队列中的订单信息
                    List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
                            Consumer.from("g1", "c1"),
                            StreamReadOptions.empty().count(1).block(Duration.ofSeconds(2)),
                            StreamOffset.create(queueName, ReadOffset.lastConsumed())
                    );
                    if (list == null || list.isEmpty())
                        continue;
//                    解析消息
                    MapRecord<String, Object, Object> record = list.get(0);
                    Map<Object, Object> value = record.getValue();
                    VoucherOrder voucherOrder = BeanUtil.fillBeanWithMap(value, new VoucherOrder(), true);
//                    创建订单
                    createVoucherOrder(voucherOrder);
//                    确认消息
                    stringRedisTemplate.opsForStream().acknowledge(queueName, "g1", record.getId());
                } catch (Exception e) {
                    log.error("处理订单异常", e);
                    handlePendingList();
                }
            }
        }

        private void handlePendingList(){
            String queueName = "stream.orders";
            while (true){
                try {
//                获取pending-list中的订单信息
                    List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
                            Consumer.from("g1", "c1"),
                            StreamReadOptions.empty().count(1),
                            StreamOffset.create(queueName, ReadOffset.from("0"))
                    );
                    if (list == null || list.isEmpty())
//                        没有直接俄退出
                        break;
//                    解析消息
                    MapRecord<String, Object, Object> record = list.get(0);
                    Map<Object, Object> value = record.getValue();
                    VoucherOrder voucherOrder = BeanUtil.fillBeanWithMap(value, new VoucherOrder(), true);
//                    创建订单
                    createVoucherOrder(voucherOrder);
//                    确认消息
                    stringRedisTemplate.opsForStream().acknowledge(queueName, "g1", record.getId());
                } catch (Exception e) {
                    log.error("处理pending订单异常", e);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    private void createVoucherOrder(VoucherOrder order) {
    //        一人一单
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
                userId.toString(),
                String.valueOf(orderId)
        );
        int r = result.intValue();
        if(r != 0){
            return Result.fail(r == 1 ? "库存不足！" : "不允许重复下单！");
        }
        return Result.ok(orderId);
    }


    /*@Override
    public Result seckillVoucher(Long voucherId) {
        Long userId = UserHolder.getUser().getId();
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
        long orderId = redisIdWorker.nextId("order");
//        添加到阻塞队列中
        VoucherOrder voucherOrder = new VoucherOrder();
        voucherOrder.setId(orderId);
        voucherOrder.setUserId(userId);
        voucherOrder.setVoucherId(voucherId);
        orderTasks.add(voucherOrder);
        return Result.ok(orderId);
    }*/

/*    @Override
    public Result seckillVoucher(Long voucherId) {
//        查询优惠券
        SeckillVoucher voucher = seckillVoucherService.getById(voucherId);
//        判断秒杀是否开始结束
        if (voucher.getBeginTime().isAfter(LocalDateTime.now())) {
//            尚未开始
            return Result.fail("秒杀尚未开始！");
        }
//        判断库存
        int stock = voucher.getStock();
        if(stock < 1) {
            return Result.fail("库存不足！");
        }
        return createVoucherOrder(voucherId);
    }

    @Transactional
    public Result createVoucherOrder(Long voucherId) {
        //        一人一单
        Long userId = UserHolder.getUser().getId();
//        创建锁对象
        RLock redisLock = redissonClient.getLock("lock:order:" + userId);
        boolean isLock = redisLock.tryLock();//空参，失败直接结束，参数：获取锁的最大等待时间、锁自动释放时间、时间单位
        if (!isLock) {
//            获取锁失败
            return Result.fail("不允许重复下单");
        }
        try {
            int count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
            if(count > 0)
                return Result.fail("不允许重复购买！");
//        扣减库存，乐观锁
            boolean success = seckillVoucherService.update()
                    .setSql("stock = stock - 1")
                    .eq("voucher_id", voucherId)
                    .gt("stock",0)//大于0就能执行
                    .update();
            if(!success){
                return Result.fail("库存不足！");
            }
//        生成订单
            VoucherOrder voucherOrder = new VoucherOrder();
            long orderId = redisIdWorker.nextId("order");
            voucherOrder.setId(orderId);
            voucherOrder.setUserId(userId);
            voucherOrder.setVoucherId(voucherId);
            save(voucherOrder);
            return Result.ok(orderId);
        } finally {
            redisLock.unlock();
        }
    }*/



//    实现分布式方案
    /*@Transactional
    public Result createVoucherOrder(Long voucherId) {
        //        一人一单
        Long userId = UserHolder.getUser().getId();
//        锁当前用户的id值
        SimpleRedisLock redisLock = new SimpleRedisLock("order:" + userId, stringRedisTemplate);
        boolean isLock = redisLock.tryLock(1200);
        if (!isLock) {
//            获取锁失败
            return Result.fail("不允许重复下单");
        }
        try {
            int count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
            if(count > 0)
                return Result.fail("不用重复购买！");
//        扣减库存，乐观锁
            boolean success = seckillVoucherService.update()
                    .setSql("stock = stock - 1")
                    .eq("voucher_id", voucherId)
                    .gt("stock",0)//大于0就能执行
                    .update();
            if(!success){
                return Result.fail("库存不足！");
            }
//        生成订单
            VoucherOrder voucherOrder = new VoucherOrder();
            long orderId = redisIdWorker.nextId("order");
            voucherOrder.setId(orderId);
            voucherOrder.setUserId(userId);
            voucherOrder.setVoucherId(voucherId);
            save(voucherOrder);
            return Result.ok(orderId);
        } finally {
            redisLock.unlock();
        }
    }*/


    /*
    单体情况下解决方案
    @Transactional
    public Result createVoucherOrder(Long voucherId) {
        //        一人一单
        Long userId = UserHolder.getUser().getId();
//        锁当前用户的id值
        synchronized (userId.toString().intern()){
            int count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
            if(count > 0)
                return Result.fail("不用重复购买！");
//        扣减库存，乐观锁
            boolean success = seckillVoucherService.update()
                    .setSql("stock = stock - 1")
                    .eq("voucher_id", voucherId)
                    .gt("stock",0)//大于0就能执行
                    .update();
            if(!success){
                return Result.fail("库存不足！");
            }
//        生成订单
            VoucherOrder voucherOrder = new VoucherOrder();
            long orderId = redisIdWorker.nextId("order");
            voucherOrder.setId(orderId);
            voucherOrder.setUserId(userId);
            voucherOrder.setVoucherId(voucherId);
            save(voucherOrder);
            return Result.ok(orderId);
        }
    }*/
}
