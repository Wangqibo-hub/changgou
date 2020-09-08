package com.changgou.order.service.impl;

import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-22 19:15
 * @description 购物车服务层实现类
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private SpuFeign spuFeign;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void add(Integer num, Long id, String username) {

        //如果数量<0就删除购物车数据
        if (num <= 0) {
            redisTemplate.boundHashOps("Cart_" + username).delete(id);
            return;
        }
        //获取sku
        Result<Sku> skuResult = skuFeign.selectByPrimaryKey(id);
        if (skuResult != null && skuResult.isFlag()) {
            Sku sku = skuResult.getData();
            Spu spu = spuFeign.selectByPrimaryKey(sku.getSpuId()).getData();
            //将sku转换成OrderItem
            OrderItem orderItem = sku2OrderItem(sku, spu, num);
            //将购物车数据存入redis
            redisTemplate.boundHashOps("Cart_"+username).put(id,orderItem);
        }
    }

    @Override
    public List<OrderItem> list(String username) {
        List<OrderItem> values = redisTemplate.boundHashOps("Cart_" + username).values();
        return values;
    }

    /**
    * @Description: 封装OrderItem的方法
    * @Param: [sku, spu, num]
    * @Return: com.com.changgou.order.pojo.OrderItem
    * @Author: Wangqibo
    * @Date: 2020/8/22/0022
    */
    private OrderItem sku2OrderItem(Sku sku,Spu spu,Integer num){
        OrderItem orderItem = new OrderItem();
        orderItem.setSkuId(sku.getId());
        orderItem.setSpuId(sku.getSpuId());
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);
        orderItem.setMoney(num * sku.getPrice());
        orderItem.setImage(sku.getImage());
        orderItem.setWeight(sku.getWeight()*num);
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        return orderItem;
    }
}
