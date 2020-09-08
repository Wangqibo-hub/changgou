package com.changgou.core.controller.impl;

import com.changgou.core.controller.ICoreController;
import com.changgou.core.service.CoreService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-11 18:09
 * @description 总控制层接口实现类
 */
public abstract class ICoreControllerImpl<T> implements ICoreController<T> {

    //注入service
    protected CoreService<T> service;
    //注入操作的实体类
    protected Class<T> clazz;

    public ICoreControllerImpl(CoreService<T> service, Class<T> clazz) {
        this.service = service;
        this.clazz = clazz;
    }

    @DeleteMapping("{id}")
    @Override
    public Result deleteByPrimaryKey(@PathVariable("id") Object key) {
        service.deleteByPrimaryKey(key);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @DeleteMapping
    @Override
    public Result delete(@RequestBody T record) {
        service.delete(record);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @PostMapping
    @Override
    public Result insert(@RequestBody T record) {
        service.insert(record);
        return new Result(true, StatusCode.OK, "插入成功");
    }

    @GetMapping("search/{page}/{size}")
    @Override
    public Result<PageInfo> findByPage(@PathVariable("page")Integer pageNo, @PathVariable("size")Integer sizeNo) {
        PageInfo pageInfo = service.findByPage(pageNo, sizeNo);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功",pageInfo);
    }

    @PostMapping("search/{page}/{size}")
    @Override
    public Result<PageInfo> findByPage(@PathVariable("page")Integer pageNo, @PathVariable("size")Integer sizeNo,@RequestBody T record) {
        PageInfo pageInfo = service.findByPage(pageNo, sizeNo, record);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    @GetMapping
    @Override
    public Result<List<T>> selectAll() {
        List<T> list = service.selectAll();
        return new Result<List<T>>(true, StatusCode.OK, "查询成功", list);
    }

    @GetMapping("{id}")
    @Override
    public Result<T> selectByPrimaryKey(@PathVariable("id") Object key) {
        T result = service.selectByPrimaryKey(key);
        return new Result<T>(true, StatusCode.OK, "查询成功", result);
    }

    @PostMapping("search")
    @Override
    public Result<List<T>> select(@RequestBody T record) {
        List<T> list = service.select(record);
        return new Result<List<T>>(true, StatusCode.OK, "查询成功", list);
    }

    @PutMapping
    @Override
    public Result update(@RequestBody T record) {
        service.update(record);
        return new Result(true, StatusCode.OK, "更新成功");
    }
}
