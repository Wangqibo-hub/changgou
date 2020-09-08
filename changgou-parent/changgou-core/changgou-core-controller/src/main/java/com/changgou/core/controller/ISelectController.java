package com.changgou.core.controller;

import entity.Result;

import java.util.List;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-11 17:49
 * @description 查询控制层接口
 */
public interface ISelectController<T> {

    /**
    * @Description: 查询所有
    * @Param: []
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/11/0011
    */
    Result<List<T>> selectAll();

    /**
    * @Description: 根据主键查询
    * @Param: [key]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/11/0011
    */
    Result<T> selectByPrimaryKey(Object key);

    /**
    * @Description: 根据条件查询
    * @Param: [record]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/11/0011
    */
    Result<List<T>> select(T record);
}
