package com.changgou.goods.service;

import com.changgou.core.service.CoreService;
import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Spu;

/****
 * @Author:admin
 * @Description:Spu业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface SpuService extends CoreService<Spu> {

    /**
    * @Description: 保存商品
    * @Param: [goods]
    * @Return: void
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    void saveGoods(Goods goods);

    /**
    * @Description: 根据id获取商品
    * @Param: [id]
    * @Return: com.com.changgou.goods.pojo.Goods
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    Goods findGoodsById(Long id);

    /**
    * @Description: 商品审核
    * @Param: [spuId]
    * @Return: void
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    void audit(Long spuId);

    /**
    * @Description: 下架商品
    * @Param: [spuId]
    * @Return: void
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    void pull(Long spuId);

    /**
    * @Description: 上架商品
    * @Param: [spuId]
    * @Return: void
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    void put(Long spuId);

    /**
    * @Description: 批量上架
    * @Param: []
    * @Return: void
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    int putMany(Long[] ids);

    /**
    * @Description: 批量下架
    * @Param: [ids]
    * @Return: int
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    int pullMany(Long[] ids);

    /**
    * @Description: 逻辑删除商品
    * @Param: [id]
    * @Return: void
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    void logicDelete(Long id);

    /**
    * @Description: 还原删除的商品
    * @Param: [id]
    * @Return: void
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    void restore(Long id);

    /**
    * @Description: 物理删除
    * @Param: [id]
    * @Return: void
    * @Author: Wangqibo
    * @Date: 2020/8/12/0012
    */
    void Delete(Long id);
}
