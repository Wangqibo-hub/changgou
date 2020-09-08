package com.changgou.goods.dao;
import com.changgou.goods.pojo.Para;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:admin
 * @Description:Para的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface ParaMapper extends Mapper<Para> {

    /**
    * @Description: 根据分类id查询参数列表
    * @Param: [cid]
    * @Return: java.util.List<com.com.changgou.goods.pojo.Para>
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    @Select("SELECT tp.* from tb_para tp,tb_category tc WHERE tp.template_id = tc.template_id AND tc.id = #{cid};")
    List<Para> findByCategoryId(Integer cid);
}
