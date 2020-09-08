package com.changgou.goods.dao;
import com.changgou.goods.pojo.Brand;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:admin
 * @Description:Brand的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface BrandMapper extends Mapper<Brand> {

    /**
    * @Description: 根据分类id查询品牌
    * @Param: [cid]
    * @Return: java.util.List<com.com.changgou.goods.pojo.Brand>
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    @Select("select tbb.* from tb_brand tbb,tb_category_brand tcb where tbb.id = tcb.brand_id and tcb.category_id = #{cid}")
    List<Brand> findByCategoryId(Integer cid);
}
