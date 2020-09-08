package com.changgou.core.controller;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-11 18:07
 * @description 总控制层接口
 */
public interface ICoreController<T> extends
        IInsertController<T>,
        IDeleteController<T>,
        ISelectController<T>,
        IUpdateController<T>,
        IPagingController<T>{
}

