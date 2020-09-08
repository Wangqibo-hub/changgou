package com.changgou.core.controller;

import com.github.pagehelper.PageInfo;
import entity.Result;

import java.util.List;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-11 18:01
 * @description 分页查询控制层接口
 */
public interface IPagingController<T> {

    /**
    * @Description: 无条件的分页查询
    * @Param: [pageNo, sizeNo]
    * @Return: entity.Result<T>
    * @Author: Wangqibo
    * @Date: 2020/8/11/0011
    */
    Result<PageInfo> findByPage(Integer pageNo, Integer sizeNo);

    /**
    * @Description: 带条件的分页查询
    * @Param: [pageNo, sizeNo, record]
    * @Return: entity.Result<java.util.List<T>>
    * @Author: Wangqibo
    * @Date: 2020/8/11/0011
    */
    Result<PageInfo> findByPage(Integer pageNo,Integer sizeNo,T record);
}
