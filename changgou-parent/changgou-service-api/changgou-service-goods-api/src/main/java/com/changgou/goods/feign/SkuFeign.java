package com.changgou.goods.feign;

import com.changgou.goods.pojo.Sku;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-15 17:27
 * @description SkuFeign
 */
@FeignClient(name = "goods")
@RequestMapping("sku")
public interface SkuFeign {

    @GetMapping("status/{status}")
    Result<List<Sku>> findByStatus(@PathVariable("status") String status);

    /**
     * 根据条件搜索
     * @param sku
     * @return
     */
    @PostMapping(value = "/search" )
    Result<List<Sku>> findList(@RequestBody(required = false) Sku sku);

    /***
     * 根据ID查询SKU信息
     * @param : sku的ID
     */
    @GetMapping("{id}")
    Result<Sku> selectByPrimaryKey(@PathVariable("id") Object key);

    @GetMapping("decCount")
    Result decCount(@RequestParam("id") Long id,@RequestParam("num") Integer num);
}
