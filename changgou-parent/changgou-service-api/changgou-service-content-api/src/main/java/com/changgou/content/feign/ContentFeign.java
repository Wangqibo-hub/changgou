package com.changgou.content.feign;

import com.changgou.content.pojo.Content;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-13 20:45
 * @description
 */
@FeignClient(name = "content")
@RequestMapping("content")
public interface ContentFeign {

    @GetMapping("list/category/{id}")
    Result<List<Content>> findByCategoryId(@PathVariable("id") Long id);
}
