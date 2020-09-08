package com.changgou.content.service;
import com.changgou.content.pojo.Content;
import com.changgou.core.service.CoreService;

import java.util.List;

/****
 * @Author:admin
 * @Description:Content业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface ContentService extends CoreService<Content> {

    /**
    * @Description: 根据分类id查询广告
    * @Param: [id]
    * @Return: java.util.List<com.com.changgou.content.pojo.Content>
    * @Author: Wangqibo
    * @Date: 2020/8/13/0013
    */
    List<Content> findByCategoryId(Long id);
}
