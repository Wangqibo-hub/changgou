package com.changgou.core.service;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-11 16:29
 * @description 新增服务层接口
 */
public interface InsertService<T> {

    /**
    * @Description: 新增记录
    * @Param: [record]
    * @Return: java.lang.Integer
    * @Author: Wangqibo
    * @Date: 2020/8/11/0011
    */
    Integer insert(T record);
}
