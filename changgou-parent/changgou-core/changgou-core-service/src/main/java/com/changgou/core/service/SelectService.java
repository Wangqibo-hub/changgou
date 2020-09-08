package com.changgou.core.service;

import java.util.List;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-11 16:21
 * @description 查询服务层接口
 */
public interface SelectService<T> {
    
    /**
    * @Description: 查询所有
    * @Param: []
    * @Return: java.util.List<T>
    * @Author: Wangqibo
    * @Date: 2020/8/11/0011
    */
   List<T> selectAll();
    
    /**
    * @Description: 根据主键查询
    * @Param: [key]
    * @Return: T
    * @Author: Wangqibo
    * @Date: 2020/8/11/0011
    */
    T selectByPrimaryKey(Object key);
    
    /**
    * @Description: 按条件查询
    * @Param: [record]
    * @Return: java.util.List<T>
    * @Author: Wangqibo
    * @Date: 2020/8/11/0011
    */
    List<T> select(T record);
}
