package com.changgou.core.controller;

import entity.Result;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-11 17:53
 * @description 更新控制层接口
 */
public interface IUpdateController<T> {

    /**
    * @Description: 更新记录
    * @Param: [record]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/11/0011
    */
    Result update(T record);
}
