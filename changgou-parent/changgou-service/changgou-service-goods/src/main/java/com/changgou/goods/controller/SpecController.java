package com.changgou.goods.controller;

import com.changgou.core.controller.impl.ICoreControllerImpl;
import com.changgou.goods.pojo.Spec;
import com.changgou.goods.service.SpecService;
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
@RequestMapping("/spec")
@CrossOrigin
public class SpecController extends ICoreControllerImpl<Spec>{

    private SpecService  specService;

    @Autowired
    public SpecController(SpecService  specService) {
        super(specService, Spec.class);
        this.specService = specService;
    }

    /**
    * @Description: 根基分类id查询规格信息
    * @Param: [cid]
    * @Return: entity.Result<java.util.List<com.com.changgou.goods.pojo.Spec>>
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    @GetMapping("category/{cid}")
    public Result<List<Spec>> findByCategoryId(@PathVariable("cid") Integer cid){
        List<Spec> specList = specService.findByCategoryId(cid);
        return new Result<List<Spec>>(true, StatusCode.OK, "查询规格列表成功", specList);
    }
}
