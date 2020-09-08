package com.changgou.goods.service;

import com.changgou.core.service.CoreService;
import com.changgou.goods.pojo.Spec;

import java.util.List;

/****
 * @Author:admin
 * @Description:Spec业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface SpecService extends CoreService<Spec> {

    /**
    * @Description: 根据分类id查询规格信息
    * @Param: [cid]
    * @Return: java.util.List<com.com.changgou.goods.pojo.Spec>
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    List<Spec> findByCategoryId(Integer cid);
}
