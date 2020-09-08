package com.changgou.order.controller;

import com.changgou.core.controller.impl.ICoreControllerImpl;
import com.changgou.order.config.TokenDecode;
import com.changgou.order.pojo.Order;
import com.changgou.order.service.OrderService;
import com.netflix.discovery.converters.Auto;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController extends ICoreControllerImpl<Order> {

    private OrderService orderService;
    @Autowired
    private TokenDecode tokenDecode;

    @Autowired
    public OrderController(OrderService orderService) {
        super(orderService, Order.class);
        this.orderService = orderService;
    }

    /**
    * @Description: 添加订单
    * @Param: [record]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/23/0023
    */
    @Override
    @PostMapping
    public Result insert(@RequestBody Order record) {
        //获取用户名
        String username = tokenDecode.getUsername();
        //设置购买用户
        record.setUsername(username);
        orderService.insert(record);
        return new Result(true, StatusCode.OK, "添加订单成功");
    }
}
