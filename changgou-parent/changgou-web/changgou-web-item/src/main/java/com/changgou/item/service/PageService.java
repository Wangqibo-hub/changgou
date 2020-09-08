package com.changgou.item.service;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-19 08:31
 * @description 静态页服务接口
 */
public interface PageService {
    /**
     * 根据商品的ID 生成静态页
     * @param spuId
     */
    void createPageHtml(Long spuId) ;
}
