package com.changgou.core.service;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-11 16:30
 * @description 删除服务层接口
 */
public interface DeleteService<T> {

    /**
    * @Description: 根据主键删除
    * @Param: [key]
    * @Return: java.lang.Integer
    * @Author: Wangqibo
    * @Date: 2020/8/11/0011
    */
    Integer deleteByPrimaryKey(Object key);
    
    /**
    * @Description: 根据条件删除
    * @Param: [record]
    * @Return: java.lang.Integer
    * @Author: Wangqibo
    * @Date: 2020/8/11/0011
    */
    Integer delete(T record);
}
