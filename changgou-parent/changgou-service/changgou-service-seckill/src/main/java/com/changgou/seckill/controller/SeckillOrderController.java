package com.changgou.seckill.controller;

import com.changgou.core.controller.impl.ICoreControllerImpl;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;
import com.changgou.seckill.service.SeckillOrderService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/seckillOrder")
@CrossOrigin
public class SeckillOrderController extends ICoreControllerImpl<SeckillOrder>{

    private SeckillOrderService  seckillOrderService;

    @Autowired
    public SeckillOrderController(SeckillOrderService  seckillOrderService) {
        super(seckillOrderService, SeckillOrder.class);
        this.seckillOrderService = seckillOrderService;
    }

    /**
     * @Description: 下单
     * @Param: [time, id]
     * @Return: entity.Result
     * @Author: Wangqibo
     * @Date: 2020/8/26/0026
     */
    @RequestMapping("add")
    public Result add(String time, Long id) {
        //获取用户名
        String username = "zhangsan";
        //下单
        boolean flag = seckillOrderService.add(time, id, username);
        if (flag) {
            return new Result(true, StatusCode.OK, "排队成功");
        }
        return new Result(false, StatusCode.ERROR, "排队失败");
    }

    /**
    * @Description: 查询抢购状态
    * @Param: []
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/26/0026
    */
    @RequestMapping("query")
    public Result queryStatus(){
        //获取用户名
        String username = "zhangsan";//tokenDcode.getUserInfo().get("username")"";

        //根据用户名查询用户抢购状态
        SeckillStatus seckillStatus = seckillOrderService.queryStatus(username);

        if(seckillStatus!=null){
            return new Result(true,StatusCode.OK,"查询抢购状态成功",seckillStatus);
        }
        //NOTFOUNDERROR =20006,没有对应的抢购数据
        return new Result(false,StatusCode.NOTFOUNDERROR,"没有抢购信息");
    }
}
