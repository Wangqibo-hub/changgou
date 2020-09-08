package com.changgou.goods.service;

import com.changgou.core.service.CoreService;
import com.changgou.goods.pojo.Para;

import java.util.List;

/****
 * @Author:admin
 * @Description:Para业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface ParaService extends CoreService<Para> {

    /**
    * @Description: 根据分类id查询分类列表
    * @Param: [cid]
    * @Return: java.util.List<com.com.changgou.goods.pojo.Para>
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    List<Para> findByCategoryId(Integer cid);
}
