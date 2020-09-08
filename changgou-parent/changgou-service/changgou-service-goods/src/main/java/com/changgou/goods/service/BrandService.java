package com.changgou.goods.service;

import com.changgou.core.service.CoreService;
import com.changgou.goods.pojo.Brand;

import java.util.List;

/****
 * @Author:admin
 * @Description:Brand业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface BrandService extends CoreService<Brand> {

    /**
    * @Description: 根据分类Id查询品牌
    * @Param: [cid]
    * @Return: java.util.List<com.com.changgou.goods.pojo.Brand>
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    List<Brand> findByCategoryId(Integer cid);
}
