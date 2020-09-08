package com.changgou.seckill.task;

import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;
import entity.IdWorker;
import entity.SystemConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-26 19:46
 * @description 多线程下单
 */
@Component
public class MultiThreadingCreateOrder {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Async
    public void createOrder() {

        try {
            System.out.println("准备执行...." + Thread.currentThread().getName());
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //从队列中获取排队信息
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundListOps(SystemConstants.SEC_KILL_USER_QUEUE_KEY).rightPop();
        try {
            if (seckillStatus != null) {
                //时间区间
                String time = seckillStatus.getTime();
                //用户登录名
                String username = seckillStatus.getUsername();
                //用户抢购商品
                Long id = seckillStatus.getGoodsId();

                //从redis中获取商品数据
                SeckillGoods goods = (SeckillGoods) redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).get(id);
                //创建订单
                SeckillOrder seckillOrder = new SeckillOrder();
                seckillOrder.setId(idWorker.nextId());
                seckillOrder.setSeckillId(id);
                seckillOrder.setMoney(goods.getCostPrice());
                seckillOrder.setUserId(username);
                seckillOrder.setCreateTime(new Date());
                seckillOrder.setStatus("0");
                //将订单存入redis中
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_ORDER_KEY).put(username, seckillOrder);
                //减库存
                //goods.setStockCount(goods.getStockCount() - 1);
                //判断当前商品是否还有库存
                if (goods.getStockCount() <= 0) {
                    //并且将商品数据同步到MySQL中
                    seckillGoodsMapper.updateByPrimaryKeySelective(goods);
                    //如果没有库存,则清空Redis缓存中该商品
                    redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).delete(id);
                } else {
                    //如果有库存，则直数据重置到Reids中
                    //redisTemplate.boundHashOps(SystemConstants.SEC_KILL_ORDER_KEY + time).put(id, goods);
                }

                //抢单成功，更新抢单状态,排队->等待支付
                seckillStatus.setStatus(2);
                seckillStatus.setOrderId(seckillOrder.getId());
                seckillStatus.setMoney(Float.valueOf(seckillOrder.getMoney()));
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_STATUS_KEY).put(username,seckillStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
