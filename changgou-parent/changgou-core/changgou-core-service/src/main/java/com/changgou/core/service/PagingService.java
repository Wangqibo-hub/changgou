package com.changgou.core.service;

import com.github.pagehelper.PageInfo;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-11 16:39
 * @description 分页查询服务层接口
 */
public interface PagingService<T> {

    /**
    * @Description: 无条件分页查询
    * @Param: [pageNo, SizeNo]
    * @Return: com.github.pagehelper.PageInfo
    * @Author: Wangqibo
    * @Date: 2020/8/11/0011
    */
    PageInfo findByPage(Integer pageNo,Integer sizeNo);
    
    /**
    * @Description: 根据条件分页查询
    * @Param: [pageNo, SizeNo, record]
    * @Return: com.github.pagehelper.PageInfo
    * @Author: Wangqibo
    * @Date: 2020/8/11/0011
    */
    PageInfo findByPage(Integer pageNo,Integer sizeNo,T record);
    
    /**
    * @Description: 根据条件分页查询
    * @Param: [pageNo, SizeNo, example]
    * @Return: com.github.pagehelper.PageInfo
    * @Author: Wangqibo
    * @Date: 2020/8/11/0011
    */
    PageInfo findByPageExample(Integer pageNo,Integer sizeNo,Object example);
}
