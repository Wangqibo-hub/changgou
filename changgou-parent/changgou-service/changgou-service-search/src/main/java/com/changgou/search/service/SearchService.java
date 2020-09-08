package com.changgou.search.service;

import java.util.Map;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-15 17:33
 * @description 搜索服务层接口
 */
public interface SearchService {

    /**
    * @Description: 将数据导入到es
    * @Param: []
    * @Return: void
    * @Author: Wangqibo
    * @Date: 2020/8/15/0015
    */
    void importToEs();

    /**
    * @Description: 搜索商品
    * @Param: [searchMap]
    * @Return: java.util.Map<java.lang.String,java.lang.Object>
    * @Author: Wangqibo
    * @Date: 2020/8/15/0015
    */
    Map<String, Object> search(Map<String, Object> searchMap);
}
