package com.changgou.core.service;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-11 16:32
 * @description 更新服务层接口
 */
public interface UpdateService<T> {
    
    /**
    * @Description: 更新记录
    * @Param: [record]
    * @Return: java.lang.Integer
    * @Author: Wangqibo
    * @Date: 2020/8/11/0011
    */
    Integer update(T record);
}
