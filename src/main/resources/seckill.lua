-- 优惠券id
local voucherId = ARGV[1]
-- 用户id
local userId = ARGV[2]
-- 订单id
local orderId = ARGV[3]

--库存key
local stockKey = 'seckill:stock:' .. voucherId
--订单key
local orderKey = 'seckill:order:' .. voucherId


--判断库存是否充足
if(tonumber(redis.call('get', stockKey)) <= 0) then
    --库存不足
    return 1
end
--判断用户是否下单 sismember
if(redis.call('sismember', orderKey, userId) == 1) then
    -- 存在，说明是重复下单
    return 2
end

--可以执行下单
redis.call('incrby', stockKey, -1)
redis.call('sadd', orderKey, userId)
--发送消息
redis.call('xadd', 'stream.orders', '*', 'userId', userId, 'voucherId', voucherId, 'id', orderId)
return 0
