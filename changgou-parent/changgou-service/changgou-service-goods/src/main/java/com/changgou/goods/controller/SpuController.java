package com.changgou.goods.controller;

import com.changgou.core.controller.impl.ICoreControllerImpl;
import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.goods.service.SkuService;
import com.changgou.goods.service.SpuService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.util.List;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/spu")
@CrossOrigin
public class SpuController extends ICoreControllerImpl<Spu>{

    private SpuService  spuService;
    @Autowired
    private SkuService skuService;

    @Autowired
    public SpuController(SpuService  spuService) {
        super(spuService, Spu.class);
        this.spuService = spuService;
    }

    /**
    * @Description: 保存商品信息
    * @Param: [goods]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    @PostMapping("save")
    public Result saveGoods(@RequestBody Goods goods){
        spuService.saveGoods(goods);
        return new Result(true, StatusCode.OK, "保存商品成功");
    }

    /**
    * @Description: 根据id查询商品
    * @Param: [id]
    * @Return: entity.Result<com.com.changgou.goods.pojo.Goods>
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    @GetMapping("goods/{id}")
    public Result<Goods> findGoodsById(@PathVariable("id") Long id){
        Goods goods = spuService.findGoodsById(id);
        return new Result<Goods>(true, StatusCode.OK, "查询商品成功", goods);
    }

    /**
    * @Description: 更新sku库存数量
    * @Param: [skuList]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    @PostMapping("updateSkuNum")
    public Result updateSkuNum(@RequestBody() Goods goods){
        List<Sku> skuList = goods.getSkuList();
        for (Sku sku : skuList) {
            skuService.update(sku);
        }
        return new Result(true, StatusCode.OK, "更新库存成功");
    }

    /**
    * @Description: 商品审核
    * @Param: [spuId]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    @PutMapping("audit/{id}")
    public Result audit(@PathVariable("id") Long spuId){
        spuService.audit(spuId);
        return new Result(true, StatusCode.OK, "审核成功");
    }

    /**
    * @Description: 下架商品
    * @Param: [spuId]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    @PutMapping("pull/{id}")
    public Result pull(@PathVariable("id") Long spuId){
        spuService.pull(spuId);
        return new Result(true, StatusCode.OK, "商品下架成功");
    }

    /**
    * @Description: 上架商品
    * @Param: [spuId]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    @PutMapping("put/{id}")
    public Result put(@PathVariable("id") Long spuId){
        spuService.put(spuId);
        return new Result(true, StatusCode.OK, "商品上架成功");
    }

    /**
    * @Description: 批量上架
    * @Param: [ids]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    @PutMapping("put/many")
    public Result putMany(@RequestBody Long[] ids){
        int count = spuService.putMany(ids);
        return new Result(true, StatusCode.OK, "成功上架"+count+"个商品");
    }

    /**
    * @Description: 批量下架
    * @Param: [ids]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    @PutMapping("pull/many")
    public Result pullMany(@RequestBody Long[] ids){
        int count = spuService.pullMany(ids);
        return new Result(true, StatusCode.OK, "成功下架"+count+"个商品");
    }

    /**
    * @Description: 逻辑删除商品
    * @Param: [id]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    @DeleteMapping("logic/delete/{id}")
    public Result logicDelete(@PathVariable("id") Long id){
        spuService.logicDelete(id);
        return new Result(true, StatusCode.OK, "逻辑删除成功");
    }

    /**
    * @Description: 还原被删除的商品
    * @Param: [id]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    @PutMapping("restore/{id}")
    public Result restore(@PathVariable("id") Long id){
        spuService.restore(id);
        return new Result(true, StatusCode.OK, "商品还原成功");
    }

    /**
    * @Description: 物理删除
    * @Param: [id]
    * @Return: entity.Result
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    @DeleteMapping("delete/{id}")
    public Result Delete(@PathVariable("id") Long id){
        spuService.Delete(id);
        return new Result(true, StatusCode.OK, "物理删除成功");
    }
}
