package com.changgou.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.exception.MyException;
import com.changgou.goods.dao.BrandMapper;
import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.dao.SkuMapper;
import com.changgou.goods.dao.SpuMapper;
import com.changgou.goods.pojo.*;
import com.changgou.goods.service.SpuService;
import entity.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/****
 * @Author:admin
 * @Description:Spu业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class SpuServiceImpl extends CoreServiceImpl<Spu> implements SpuService {

    private SpuMapper spuMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private IdWorker idWorker;

    @Autowired
    public SpuServiceImpl(SpuMapper spuMapper) {
        super(spuMapper, Spu.class);
        this.spuMapper = spuMapper;
    }

    @Override
    @Transactional
    public void saveGoods(Goods goods) {
        Spu spu = goods.getSpu();
        if (spu.getId() != null) {
            //有id就是修改
            //1. 修改spu信息
            spuMapper.updateByPrimaryKeySelective(spu);
            //2.删除原先的sku
            Sku sku = new Sku();
            sku.setSpuId(spu.getId());
            skuMapper.delete(sku);
        } else {
            //没有id就是新增
            //1.封装spu信息，并保存
            //1.1主需要封装主键就行
            long spuId = idWorker.nextId();
            spu.setId(spuId);
            spuMapper.insertSelective(spu);
        }
        //2.封装sku信息，并保存
        Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());
        Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
        List<Sku> skuList = goods.getSkuList();
        for (Sku sku : skuList) {
            //2.1封装id
            long skuId = idWorker.nextId();
            sku.setId(skuId);
            //2.2封装sku名称
            String spec = sku.getSpec();
            if (StringUtils.isEmpty(spec)) {
                sku.setSpec("{}");
            }
            StringBuffer buffer = new StringBuffer();
            buffer.append(spu.getName());
            Map<String, String> map = JSON.parseObject(spec, Map.class);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                buffer.append(" ");
                buffer.append(entry.getValue());
            }
            sku.setName(buffer.toString());
            //2.3 封装创建时间
            sku.setCreateTime(new Date());
            //2.4 封装更新时间
            sku.setUpdateTime(sku.getCreateTime());
            //2.5 封装spuId
            sku.setSpuId(spu.getId());
            //2.6 封装类目ID
            sku.setCategoryId(spu.getCategory3Id());
            //2.7 封装类目名称
            sku.setCategoryName(category.getName());
            //2.8 封装品牌名称
            sku.setBrandName(brand.getName());
            //保存数据
            skuMapper.insertSelective(sku);
        }
    }

    @Override
    public Goods findGoodsById(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> skuList = skuMapper.select(sku);
        Goods goods = new Goods(spu, skuList);
        return goods;
    }

    @Override
    public void audit(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        //判断是否是已删除商品
        if (spu.getIsDelete().equalsIgnoreCase("1")) {
            throw new RuntimeException("该商品已删除");
        }
        //没有删除，实现审核
        spu.setStatus("1");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    @Override
    public void pull(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        //判断商品是否删除
        if (spu.getIsDelete().equalsIgnoreCase("1")) {
            throw new RuntimeException("该商品已删除");
        }
        //没有删除，执行下架商品
        spu.setIsMarketable("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    @Override
    public void put(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        //判断商品是否删除
        if (spu.getIsDelete().equalsIgnoreCase("1")) {
            throw new RuntimeException("该商品已删除");
        }
        //判断商品是否审核通过
        if (spu.getStatus().equalsIgnoreCase("0")) {
            throw new RuntimeException("该商品尚未审核，不能上架");
        }
        //没有删除，且审核通过，执行上架商品
        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    @Override
    public int putMany(Long[] ids) {
        Spu spu = new Spu();
        spu.setIsMarketable("1");
        //设置模板
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //id范围
        criteria.andIn("id", Arrays.asList(ids));
        //状态为下架的商品
        criteria.andEqualTo("isMarketable", "0");
        //审核通过的商品
        criteria.andEqualTo("status", "1");
        //非删除的商品
        criteria.andEqualTo("isDelete", "0");
        int count = spuMapper.updateByExampleSelective(spu, example);
        return count;
    }

    @Override
    public int pullMany(Long[] ids) {
        Spu spu = new Spu();
        spu.setIsMarketable("0");
        //设置模板
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //id范围
        criteria.andIn("id", Arrays.asList(ids));
        //状态为上架的商品
        criteria.andEqualTo("isMarketable", "1");
        //审核通过的商品
        criteria.andEqualTo("status", "1");
        //非删除的商品
        criteria.andEqualTo("isDelete", "0");
        int count = spuMapper.updateByExampleSelective(spu, example);
        return count;
    }

    @Override
    public void logicDelete(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //判断是否下架
        if (spu.getIsMarketable().equals("1")) {
            throw new RuntimeException("该商品没有下架，不能删除");
        }
        //删除
        spu.setIsDelete("1");
        //修改审核状态
        spu.setStatus("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    @Override
    public void restore(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //判断是否是删除的商品
        if (spu.getIsDelete().equals("0")) {
            throw new RuntimeException("该商品没有删除");
        }
        //恢复到未删除
        spu.setIsDelete("0");
        //修改审核状态
        spu.setStatus("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    @Override
    public void Delete(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //判断是否逻辑删除
        if (spu.getIsDelete().equals("0")) {
            throw new RuntimeException("该商品不能删除");
        }
        //删除
        spuMapper.delete(spu);
    }
}
