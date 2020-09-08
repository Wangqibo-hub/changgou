package com.changgou.goods.service;

import com.changgou.core.service.CoreService;
import com.changgou.goods.pojo.Template;

/****
 * @Author:admin
 * @Description:Template业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface TemplateService extends CoreService<Template> {

    /**
    * @Description: 根据分类id查询模板数据
    * @Param: [cid]
    * @Return: com.com.changgou.goods.pojo.Template
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    Template findByCategoryId(Integer cid);
}
