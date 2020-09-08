package com.changgou.goods.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.goods.dao.ParaMapper;
import com.changgou.goods.dao.TemplateMapper;
import com.changgou.goods.pojo.Para;
import com.changgou.goods.pojo.Template;
import com.changgou.goods.service.ParaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/****
 * @Author:admin
 * @Description:Para业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class ParaServiceImpl extends CoreServiceImpl<Para> implements ParaService {

    private ParaMapper paraMapper;
    @Autowired
    private TemplateMapper templateMapper;

    @Autowired
    public ParaServiceImpl(ParaMapper paraMapper) {
        super(paraMapper, Para.class);
        this.paraMapper = paraMapper;
    }

    @Override
    public Integer insert(Para record) {
        int count = paraMapper.insertSelective(record);
        updateParaNum(record, 1);
        return count;
    }

    @Override
    public Integer delete(Para record) {
        int count = paraMapper.delete(record);
        updateParaNum(record, -1);
        return count;
    }

    @Override
    public Integer deleteByPrimaryKey(Object key) {
        Para para = paraMapper.selectByPrimaryKey(key);
        int count = paraMapper.deleteByPrimaryKey(key);
        updateParaNum(para, -1);
        return count;
    }

    /**
     * @Description: 更新模板表
     * @Param: [para, count]
     * @Return: void
     * @Author: Wangqibo
     * @Date: 2020/8/11/0011
     */
    private void updateParaNum(Para para, Integer count){
        Template template = templateMapper.selectByPrimaryKey(para.getTemplateId());
        Integer paraNum = template.getParaNum();
        template.setParaNum(paraNum + count);
        templateMapper.updateByPrimaryKeySelective(template);
    }

    @Override
    public List<Para> findByCategoryId(Integer cid) {
        List<Para> paraList = paraMapper.findByCategoryId(cid);
        return paraList;
    }
}
