package com.changgou.order.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.order.dao.OrderItemMapper;
import com.changgou.order.dao.OrderMapper;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import com.changgou.order.service.OrderService;
import com.changgou.user.feign.UserFeign;
import entity.IdWorker;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/****
 * @Author:admin
 * @Description:Order业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class OrderServiceImpl extends CoreServiceImpl<Order> implements OrderService {

    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private CartService cartService;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private UserFeign userFeign;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper) {
        super(orderMapper, Order.class);
        this.orderMapper = orderMapper;
    }

    @Override
    @GlobalTransactional
    public Integer insert(Order record) {
        //查询出用户的所有购物车
        List<OrderItem> orderItemList = cartService.list(record.getUsername());
        //统计计算
        int totalMoney = 0;
        int totalPayMoney = 0;
        int num = 0;
        for (OrderItem orderItem : orderItemList) {
            //总金额
            totalMoney += orderItem.getMoney();
            //实际支付金额
            //totalPayMoney += orderItem.getPayMoney();
            //总数量
            num += orderItem.getNum();
        }
        record.setTotalNum(num);
        record.setTotalMoney(totalMoney);
        record.setPayMoney(totalPayMoney);
        record.setPayMoney(totalMoney - totalPayMoney);

        //其他数据完善
        record.setCreateTime(new Date());
        record.setUpdateTime(record.getCreateTime());
        record.setBuyerRate("0");        //0:未评价，1：已评价
        record.setSourceType("1");       //来源，1：WEB
        record.setOrderStatus("0");      //0:未完成,1:已完成，2：已退货
        record.setPayStatus("0");        //0:未支付，1：已支付，2：支付失败
        record.setConsignStatus("0");    //0:未发货，1：已发货，2：已收货
        record.setId("NO." + idWorker.nextId());
        int count = orderMapper.insertSelective(record);

        //添加订单明细
        for (OrderItem orderItem : orderItemList) {
            orderItem.setId("NO." + idWorker.nextId());
            orderItem.setIsReturn("0");
            orderItem.setOrderId(record.getId());
            orderItemMapper.insertSelective(orderItem);
            Long skuId = orderItem.getSkuId();
            //减库存
            skuFeign.decCount(skuId, orderItem.getNum());
        }

        //增加用户积分
        userFeign.addPoints(record.getUsername(), 10);
        //清除Redis缓存购物车数据
        redisTemplate.delete("Cart_" + record.getUsername());
        return count;
    }
}
