package com.changgou.goods.controller;

import com.changgou.core.controller.impl.ICoreControllerImpl;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.service.SkuService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/sku")
@CrossOrigin
public class SkuController extends ICoreControllerImpl<Sku>{

    private SkuService  skuService;

    @Autowired
    public SkuController(SkuService  skuService) {
        super(skuService, Sku.class);
        this.skuService = skuService;
    }

    /**
    * @Description: 根据状态查询
    * @Param: [status]
    * @Return: entity.Result<java.util.List<com.com.changgou.goods.pojo.Sku>>
    * @Author: Wangqibo
    * @Date: 2020/8/15/0015
    */
    @GetMapping("status/{status}")
    public Result<List<Sku>> findByStatus(@PathVariable("status") String status){
        List<Sku> skuList = skuService.findByStatus(status);
        return new Result<List<Sku>>(true, StatusCode.OK, "查询成功", skuList);
    }

    /**
    * @Description: 下单成功后减库存
    * @Param: [id, num]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/23/0023
    */
    @GetMapping("decCount")
   public Result decCount(@RequestParam("id") Long id,@RequestParam("num") Integer num){
        int count = skuService.decCount(id,num);
        if (count > 0) {
            return new Result(true, StatusCode.OK, "扣库存成功");
        } else {
            return new Result(false, StatusCode.ERROR, "扣库存失败");
        }
   }
}
