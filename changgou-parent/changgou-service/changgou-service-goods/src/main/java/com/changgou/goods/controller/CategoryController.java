package com.changgou.goods.controller;

import com.changgou.core.controller.impl.ICoreControllerImpl;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.service.CategoryService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/category")
@CrossOrigin
public class CategoryController extends ICoreControllerImpl<Category>{

    private CategoryService  categoryService;

    @Autowired
    public CategoryController(CategoryService  categoryService) {
        super(categoryService, Category.class);
        this.categoryService = categoryService;
    }

    /**
     * @Description: 根据父id查询子节点
     * @Param: [pid]
     * @Return: entity.Result<java.util.List<com.com.changgou.goods.pojo.Category>>
     * @Author: Wangqibo
     * @Date: 2020/8/11/0011
     */
    @RequestMapping("list/{pid}")
    public Result<List<Category>> findByParentId(@PathVariable("pid") Integer pid){
        List<Category> list = categoryService.findByParentId(pid);
        return new Result<List<Category>>(true, StatusCode.OK, "查询成功", list);
    }
}
