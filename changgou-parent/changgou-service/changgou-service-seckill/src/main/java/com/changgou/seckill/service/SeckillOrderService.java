package com.changgou.seckill.service;

import com.changgou.core.service.CoreService;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;

/****
 * @Author:admin
 * @Description:SeckillOrder业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface SeckillOrderService extends CoreService<SeckillOrder> {

    boolean add(String time, Long id, String username);

    SeckillStatus queryStatus(String username);
}
