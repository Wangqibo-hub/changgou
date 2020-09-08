package com.changgou.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-18 16:47
 * @description 商品搜索Feign
 */
@FeignClient("search")
@RequestMapping("search")
public interface SkuFeign {

    @GetMapping
    Map search(@RequestParam(required = false) Map<String ,Object> searchMap);
}
