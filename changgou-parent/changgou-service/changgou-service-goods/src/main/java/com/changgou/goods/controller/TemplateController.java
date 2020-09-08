package com.changgou.goods.controller;

import com.changgou.core.controller.impl.ICoreControllerImpl;
import com.changgou.goods.pojo.Template;
import com.changgou.goods.service.TemplateService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/template")
@CrossOrigin
public class TemplateController extends ICoreControllerImpl<Template>{

    private TemplateService  templateService;

    @Autowired
    public TemplateController(TemplateService  templateService) {
        super(templateService, Template.class);
        this.templateService = templateService;
    }

    /**
    * @Description: 根据分类id查询模板数据
    * @Param: [cid]
    * @Return: entity.Result<com.com.changgou.goods.pojo.Template>
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    @GetMapping("category/{cid}")
    public Result<Template> findByCategoryId(@PathVariable("cid") Integer cid){
        Template template = templateService.findByCategoryId(cid);
        return new Result<Template>(true, StatusCode.OK, "模板数据查询成功", template);
    }
}
