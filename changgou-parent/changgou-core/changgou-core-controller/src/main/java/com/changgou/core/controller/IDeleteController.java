package com.changgou.core.controller;

import entity.Result;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-11 17:54
 * @description 删除控制层接口
 */
public interface IDeleteController<T> {

    /**
    * @Description: 根据主键删除
    * @Param: [key]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/11/0011
    */
    Result deleteByPrimaryKey(Object key);

    /**
    * @Description: 根据条件删除
    * @Param: [record]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/11/0011
    */
    Result delete(T record);
}
