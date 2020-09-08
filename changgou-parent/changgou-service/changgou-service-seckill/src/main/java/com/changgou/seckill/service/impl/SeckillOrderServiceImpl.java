package com.changgou.seckill.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;
import com.changgou.seckill.service.SeckillOrderService;
import com.changgou.seckill.task.MultiThreadingCreateOrder;
import entity.IdWorker;
import entity.SystemConstants;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/****
 * @Author:admin
 * @Description:SeckillOrder业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class SeckillOrderServiceImpl extends CoreServiceImpl<SeckillOrder> implements SeckillOrderService {

    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MultiThreadingCreateOrder multiThreadingCreateOrder;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    public SeckillOrderServiceImpl(SeckillOrderMapper seckillOrderMapper) {
        super(seckillOrderMapper, SeckillOrder.class);
        this.seckillOrderMapper = seckillOrderMapper;
    }

    @Override
    public boolean add(String time, Long id, String username) {

        //判断有没有重复排队
        Long userQueueCount = redisTemplate.boundHashOps(SystemConstants.SEC_KILL_QUEUE_REPEAT_KEY).increment(username, 1);
        if (userQueueCount > 1) {
            throw new RuntimeException("重复排队了");
        }

        //避免超卖--> 使用分布式锁
        RLock mylock = redissonClient.getLock("Mylock");
        try {
            //上锁
            mylock.lock(100, TimeUnit.SECONDS);
            dercount(id, time);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            mylock.unlock();
        }

        //排队信息封装
        SeckillStatus seckillStatus = new SeckillStatus(username, new Date(),1, id,time);

        //将秒杀抢单信息存入到Redis中,这里采用List方式存储,List本身是一个队列
        redisTemplate.boundListOps(SystemConstants.SEC_KILL_USER_QUEUE_KEY).leftPush(seckillStatus);

        //用户排队的状态设置
        redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_STATUS_KEY).put(username, seckillStatus);

        //开启多线程下单
        multiThreadingCreateOrder.createOrder();
        return true;
    }

    /**
    * @Description: 更新库存
    * @Param: [id, time]
    * @Return: void
    * @Author: Wangqibo
    * @Date: 2020/8/27/0027
    */
    private void dercount(Long id, String time) {
        SeckillGoods goods = (SeckillGoods) redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).get(id);
        //如果没有库存，则直接抛出异常
        if (goods == null || goods.getStockCount() <= 0) {
            throw new RuntimeException("已售罄!");
        }
        //减库存
        goods.setStockCount(goods.getStockCount() - 1);
        //存储到redis中
        redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).put(id, goods);

        System.out.println("库存为：" + ((SeckillGoods) redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).get(id)).getStockCount());
    }


    @Override
    public SeckillStatus queryStatus(String username) {
        return (SeckillStatus) redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_STATUS_KEY).get(username);
    }
}
