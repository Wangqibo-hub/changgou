package com.changgou.goods.service;

import com.changgou.core.service.CoreService;
import com.changgou.goods.pojo.Sku;

import java.util.List;

/****
 * @Author:admin
 * @Description:Sku业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface SkuService extends CoreService<Sku> {

    /**
    * @Description: 根据状态查询
    * @Param: [status]
    * @Return: java.util.List<com.com.changgou.goods.pojo.Sku>
    * @Author: Wangqibo
    * @Date: 2020/8/15/0015
    */
    List<Sku> findByStatus(String status);

    int decCount(Long id, Integer num);
}
