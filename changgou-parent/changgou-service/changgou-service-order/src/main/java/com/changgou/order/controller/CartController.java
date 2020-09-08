package com.changgou.order.controller;

import com.changgou.order.config.TokenDecode;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-22 19:11
 * @description 购物车控制层
 */
@RestController
@CrossOrigin
@RequestMapping("cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private TokenDecode tokenDecode;

    /**
    * @Description: 添加购物车
    * @Param: [num, id]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/22/0022
    */
    @GetMapping("add")
    public Result add(Integer num, Long id) {
        String username = tokenDecode.getUsername();
        cartService.add(num, id, username);
        return new Result(true, StatusCode.OK, "添加购物车成功");
    }

    /**
    * @Description: 查询购物车列表
    * @Param: []
    * @Return: entity.Result<java.util.List<com.com.changgou.order.pojo.OrderItem>>
    * @Author: Wangqibo
    * @Date: 2020/8/22/0022
    */
    @GetMapping("list")
    public Result<List<OrderItem>> list(){
        String username = tokenDecode.getUsername();
        List<OrderItem> orderItemList = cartService.list(username);
        return new Result<>(true, StatusCode.OK, "查询购物车列表成功", orderItemList);
    }
}
