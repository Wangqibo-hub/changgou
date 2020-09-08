package com.changgou.core.service.impl;

import com.changgou.core.service.CoreService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-11 16:47
 * @description 总服务层实现类
 */
public abstract class CoreServiceImpl<T> implements CoreService<T> {

    //注入通用Mapper
    protected Mapper<T> baseMapper;
    //注入操作的实体类
    protected Class<T> clazz;

    //构造方法，让子类来指定具体要操作的实体类
    public CoreServiceImpl(Mapper<T> baseMapper, Class clazz) {
        this.baseMapper = baseMapper;
        this.clazz = clazz;
    }

    @Override
    public Integer deleteByPrimaryKey(Object key) {
        return baseMapper.deleteByPrimaryKey(key);
    }

    @Override
    public Integer delete(T record) {
        return baseMapper.delete(record);
    }

    @Override
    public Integer insert(T record) {
        return baseMapper.insertSelective(record);
    }

    @Override
    public PageInfo findByPage(Integer pageNo, Integer sizeNo) {
        PageHelper.startPage(pageNo,sizeNo);
        List<T> all = baseMapper.selectAll();
        PageInfo<T> pageInfo = new PageInfo<T>(all);
        return pageInfo;
    }

    @Override
    public PageInfo findByPage(Integer pageNo, Integer sizeNo, T record) {
        PageHelper.startPage(pageNo, sizeNo);
        Example example = getExample(record);
        List<T> list = baseMapper.selectByExample(example);
        PageInfo<T> pageInfo = new PageInfo<T>(list);
        return pageInfo;
    }

    /**
    * @Description: 根据record创建example
    * @Param: [record]
    * @Return: tk.mybatis.mapper.entity.Example
    * @Author: Wangqibo
    * @Date: 2020/8/11/0011
    */
    private Example getExample(T record) {
        Example example = new Example(clazz);
        Example.Criteria criteria = example.createCriteria();
        //获取实例类的所有字段
        Field[] fields = record.getClass().getDeclaredFields();
        //遍历字段拼接查询条件
        for (Field field : fields) {
            try {
                //遇到Id注解和Transient注解直接跳过，不需要设置
                if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(Transient.class)) {
                    continue;
                }
                //创建属性描述器
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), record.getClass());
                //获取这个属性的值
                Object value = propertyDescriptor.getReadMethod().invoke(record);
                //如果是字符串
                if (value != null && value.getClass().getName().equals("java.lang.String")) {
                    //获取该字段上的注解
                    Column column = field.getAnnotation(Column.class);
                    //判断如果长度为1，则执行等号
                    int length = column.length();
                    if (length == 1) {
                        criteria.andEqualTo(field.getName(), value);
                    } else {
                        criteria.andLike(field.getName(), "%" + value + "%");
                    }
                }else {
                    //不是字符串
                    criteria.andEqualTo(field.getName(), value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return example;
    }

    @Override
    public PageInfo findByPageExample(Integer pageNo, Integer sizeNo, Object example) {
        PageHelper.startPage(pageNo,sizeNo);
        List<T> all = baseMapper.selectByExample(example);
        PageInfo<T> pageInfo = new PageInfo<T>(all);
        return pageInfo;
    }

    @Override
    public List<T> selectAll() {
        return baseMapper.selectAll();
    }

    @Override
    public T selectByPrimaryKey(Object key) {
        return baseMapper.selectByPrimaryKey(key);
    }

    @Override
    public List<T> select(T record) {
        Example example = getExample(record);
        return baseMapper.selectByExample(example);
    }

    @Override
    public Integer update(T record) {
        return baseMapper.updateByPrimaryKeySelective(record);
    }
}
