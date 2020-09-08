package com.changgou.goods.service;

import com.changgou.core.service.CoreService;
import com.changgou.goods.pojo.Category;

import java.util.List;

/****
 * @Author:admin
 * @Description:Category业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface CategoryService extends CoreService<Category> {

    /**
     * @Description: 根据父节点查询子节点
     * @Param: [pid]
     * @Return: java.util.List<com.com.changgou.goods.pojo.Category>
     * @Author: Wangqibo
     * @Date: 2020/8/11/0011
     */
    List<Category> findByParentId(Integer pid);
}
