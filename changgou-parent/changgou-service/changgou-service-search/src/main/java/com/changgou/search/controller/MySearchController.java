package com.changgou.search.controller;

import com.changgou.search.service.SearchService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-15 17:33
 * @description 搜索控制层
 */
@RestController
@RequestMapping("search")
public class MySearchController {

    @Autowired
    private SearchService searchService;

    /**
    * @Description: 将数据导入到es
    * @Param: []
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/15/0015
    */
    @GetMapping("import")
    public Result importToEs(){
        searchService.importToEs();
        return new Result(true, StatusCode.OK, "数据导入到es成功");
    }

    /**
    * @Description: 搜索sku
    * @Param: [searchMap]
    * @Return: entity.Result<java.util.Map<java.lang.String,java.lang.Object>>
    * @Author: Wangqibo
    * @Date: 2020/8/15/0015
    */
    @GetMapping
    public Map<String,Object> search(@RequestParam(required = false) Map<String ,Object> searchMap){
        Map<String, Object> resultMap = searchService.search(searchMap);
        return resultMap;
    }
}

