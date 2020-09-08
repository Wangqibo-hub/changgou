package com.changgou.content.controller;

import com.changgou.content.pojo.Content;
import com.changgou.content.service.ContentService;
import com.changgou.core.controller.impl.ICoreControllerImpl;
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
@RequestMapping("/content")
@CrossOrigin
public class ContentController extends ICoreControllerImpl<Content>{

    private ContentService  contentService;

    @Autowired
    public ContentController(ContentService  contentService) {
        super(contentService, Content.class);
        this.contentService = contentService;
    }

    /**
    * @Description: 根据分类id查询广告
    * @Param: [id]
    * @Return: entity.Result<java.util.List<com.com.changgou.content.pojo.Content>>
    * @Author: Wangqibo
    * @Date: 2020/8/13/0013
    */
    @GetMapping("list/category/{id}")
    public Result<List<Content>> findByCategoryId(@PathVariable("id") Long id){
        List<Content> contentList = contentService.findByCategoryId(id);
        return new Result<>(true, StatusCode.OK, "查询广告成功", contentList);
    }
}
