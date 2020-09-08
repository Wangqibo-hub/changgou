package com.changgou.goods.controller;

import com.changgou.core.controller.impl.ICoreControllerImpl;
import com.changgou.goods.pojo.Para;
import com.changgou.goods.service.ParaService;
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
@RequestMapping("/para")
@CrossOrigin
public class ParaController extends ICoreControllerImpl<Para>{

    private ParaService  paraService;

    @Autowired
    public ParaController(ParaService  paraService) {
        super(paraService, Para.class);
        this.paraService = paraService;
    }

    /**
    * @Description: 根据分类id查询参数列表信息
    * @Param: [cid]
    * @Return: entity.Result<java.util.List<com.com.changgou.goods.pojo.Para>>
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    @GetMapping("category/{cid}")
    public Result<List<Para>> findByCategoryId(@PathVariable("cid") Integer cid){
        List<Para> paraList = paraService.findByCategoryId(cid);
        return new Result<List<Para>>(true, StatusCode.OK, "查询参数列表成功", paraList);
    }
}
