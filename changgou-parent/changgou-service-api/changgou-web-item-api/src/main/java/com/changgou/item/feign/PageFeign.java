package com.changgou.item.feign;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-19 16:45
 * @description
 */
@FeignClient("item")
@RequestMapping("page")
public interface PageFeign {
    /***
     * 根据SpuID生成静态页
     * @param id
     * @return
     */
    @RequestMapping("/createHtml/{id}")
    Result createHtml(@PathVariable(name="id") Long id);
}
