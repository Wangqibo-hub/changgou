package com.changgou.core.service;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-11 16:44
 * @description 总服务层接口
 */
public interface CoreService<T> extends
        SelectService<T>,
        InsertService<T>,
        UpdateService<T>,
        DeleteService<T>,
        PagingService<T>{
}
