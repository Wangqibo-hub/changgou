package com.changgou.core.controller;

import entity.Result;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-11 17:46
 * @description 插入控制层接口
 */
public interface IInsertController<T> {

    /**
    * @Description: 插入记录
    * @Param: [record]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/11/0011
    */
    Result insert(T record);
}
