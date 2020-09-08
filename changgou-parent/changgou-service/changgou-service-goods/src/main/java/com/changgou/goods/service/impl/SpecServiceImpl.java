package com.changgou.goods.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.goods.dao.SpecMapper;
import com.changgou.goods.dao.TemplateMapper;
import com.changgou.goods.pojo.Spec;
import com.changgou.goods.pojo.Template;
import com.changgou.goods.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/****
 * @Author:admin
 * @Description:Spec业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class SpecServiceImpl extends CoreServiceImpl<Spec> implements SpecService {

    private SpecMapper specMapper;
    @Autowired
    private TemplateMapper templateMapper;

    @Autowired
    public SpecServiceImpl(SpecMapper specMapper) {
        super(specMapper, Spec.class);
        this.specMapper = specMapper;
    }

    @Override
    public Integer insert(Spec record) {
        int insert = specMapper.insertSelective(record);
        updateSpecNum(record, 1);
        return insert;
    }

    @Override
    public Integer deleteByPrimaryKey(Object key) {
        Spec spec = specMapper.selectByPrimaryKey(key);
        int delete = specMapper.deleteByPrimaryKey(key);
        updateSpecNum(spec, -1);
        return delete;
    }

    @Override
    public Integer delete(Spec record) {
        int delete = specMapper.delete(record);
        updateSpecNum(record, -1);
        return delete;
    }

    /**
    * @Description: 更新模板数据
    * @Param: [spec, count]
    * @Return: void
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    private void updateSpecNum(Spec spec,Integer count){
        Template template = templateMapper.selectByPrimaryKey(spec.getTemplateId());
        Integer specNum = template.getSpecNum();
        template.setSpecNum(specNum + count);
        templateMapper.updateByPrimaryKeySelective(template);
    }

    @Override
    public List<Spec> findByCategoryId(Integer cid) {
        List<Spec> specList = specMapper.findByCategoryId(cid);
        return specList;
    }
}
