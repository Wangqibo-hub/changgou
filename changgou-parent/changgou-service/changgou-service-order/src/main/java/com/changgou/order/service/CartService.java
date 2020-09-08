package com.changgou.order.service;

import com.changgou.order.pojo.OrderItem;

import java.util.List;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-22 19:15
 * @description 购物车服务层接口
 */
public interface CartService {

    void add(Integer num, Long id, String username);

    List<OrderItem> list(String username);
}
