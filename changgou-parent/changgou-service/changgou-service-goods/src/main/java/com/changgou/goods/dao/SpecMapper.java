package com.changgou.goods.dao;
import com.changgou.goods.pojo.Spec;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:admin
 * @Description:Spec的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface SpecMapper extends Mapper<Spec> {

    /**
    * @Description: 根据分类id查询规格信息
    * @Param: [cid]
    * @Return: java.util.List<com.com.changgou.goods.pojo.Spec>
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    @Select("SELECT ts.* from tb_spec ts,tb_category tc WHERE ts.template_id = tc.template_id AND tc.id = #{cid};")
    List<Spec> findByCategoryId(Integer cid);
}
