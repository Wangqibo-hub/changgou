package com.changgou.seckill.controller;

import com.changgou.core.controller.impl.ICoreControllerImpl;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.service.SeckillGoodsService;
import entity.DateUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/seckillGoods")
@CrossOrigin
public class SeckillGoodsController extends ICoreControllerImpl<SeckillGoods>{

    private SeckillGoodsService  seckillGoodsService;

    @Autowired
    public SeckillGoodsController(SeckillGoodsService  seckillGoodsService) {
        super(seckillGoodsService, SeckillGoods.class);
        this.seckillGoodsService = seckillGoodsService;
    }

    /**
    * @Description: 获取时间菜单
    * @Param: []
    * @Return: List<Date>
    * @Author: Wangqibo
    * @Date: 2020/8/26/0026
    */
    @RequestMapping("menus")
    public List<Date> dateMenus(){
        return DateUtil.getDateMenus();
    }

    /**
    * @Description: 获取对应时间段秒杀商品集合
    * @Param: [time]
    * @Return: entity.Result<java.util.List<com.changgou.seckill.pojo.SeckillGoods>>
    * @Author: Wangqibo
    * @Date: 2020/8/26/0026
    */
    @RequestMapping("list")
    public Result<List<SeckillGoods>> list(String time){
        List<SeckillGoods> seckillGoodsList = seckillGoodsService.list(time);
        return new Result<>(true, StatusCode.OK, "查询秒杀商品成功",seckillGoodsList);
    }

    /**
    * @Description: 查询单个秒杀商品
    * @Param: [time, id]
    * @Return: entity.Result<com.changgou.seckill.pojo.SeckillGoods>
    * @Author: Wangqibo
    * @Date: 2020/8/26/0026
    */
    @RequestMapping("one")
    public Result<SeckillGoods> one(String time,Long id){
        SeckillGoods seckillGoods = seckillGoodsService.one(time,id);
        return new Result<>(true, StatusCode.OK, "查询秒杀商品成功", seckillGoods);
    }
}
