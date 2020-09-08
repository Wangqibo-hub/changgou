package com.changgou.goods.controller;

import com.changgou.core.controller.impl.ICoreControllerImpl;
import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/brand")
@CrossOrigin
public class BrandController extends ICoreControllerImpl<Brand>{

    private BrandService  brandService;

    @Autowired
    public BrandController(BrandService  brandService) {
        super(brandService, Brand.class);
        this.brandService = brandService;
    }

    /**
    * @Description: 根据分类id查询品牌
    * @Param: [cid]
    * @Return: entity.Result<java.util.List<com.com.changgou.goods.pojo.Brand>>
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    @GetMapping("category/{cid}")
    public Result<List<Brand>> findByCategoryId(@PathVariable("cid") Integer cid){
        List<Brand> brandList = brandService.findByCategoryId(cid);
        return new Result<List<Brand>>(true, StatusCode.OK, "查询品牌成功",brandList);
    }
}
