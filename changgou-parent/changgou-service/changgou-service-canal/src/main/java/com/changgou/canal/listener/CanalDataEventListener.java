package com.changgou.canal.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.changgou.content.feign.ContentFeign;
import com.changgou.content.pojo.Content;
import com.changgou.item.feign.PageFeign;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import com.xpand.starter.canal.annotation.UpdateListenPoint;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-13 20:07
 * @description
 */
@CanalEventListener
public class CanalDataEventListener {

    @Autowired
    private ContentFeign contentFeign;
    @Autowired
    private PageFeign pageFeign;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
    * @Description: 自定义监听数据库操作
    * @Param: [entryType, rowData]
    * @Return: void
    * @Author: Wangqibo
    * @Date: 2020/8/13/0013
    */
    @ListenPoint(destination = "example",
                 schema = "changgou_content",
                 table = {"tb_content", "tb_content_category"},
                 eventType = {
                    CanalEntry.EventType.UPDATE,
                    CanalEntry.EventType.DELETE,
                    CanalEntry.EventType.INSERT})
    public void onEventCustomUpdate(CanalEntry.EventType eventType,CanalEntry.RowData rowData){
        //获取categoryId的值
        String categoryId = getCategoryId(eventType, rowData);
        //调用feign获取该分类下所有广告集合
        Result<List<Content>> result = contentFeign.findByCategoryId(Long.valueOf(categoryId));
        //更新redis
        stringRedisTemplate.boundValueOps("content_" + categoryId).set(JSON.toJSONString(result.getData()));
    }

    /**
    * @Description: 根据不同操作获取categoryId
    * @Param: [eventType, rowData]
    * @Return: java.lang.String
    * @Author: Wangqibo
    * @Date: 2020/8/13/0013
    */
    private String getCategoryId(CanalEntry.EventType eventType,CanalEntry.RowData rowData){
        String categoryId = "";
        if (eventType == CanalEntry.EventType.DELETE) {
            //如果是删除，获取beforeList
            List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
            for (CanalEntry.Column column : beforeColumnsList) {
                if (column.getName().equalsIgnoreCase("category_id")) {
                    categoryId = column.getValue();
                    return categoryId;
                }
            }
        }else {
            //如果是添加和修改，获取afterList
            List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
            for (CanalEntry.Column column : afterColumnsList) {
                if (column.getName().equalsIgnoreCase("category_id")) {
                    categoryId = column.getValue();
                    return categoryId;
                }
            }
        }
        return categoryId;
    }

    /**
     * 监听spu变化
     * @param eventType
     * @param rowData
     */
    @ListenPoint(destination = "example",
            schema = "changgou_goods",
            table = {"tb_spu"},
            eventType = {CanalEntry.EventType.UPDATE, CanalEntry.EventType.INSERT, CanalEntry.EventType.DELETE})
    public void onEventCustomSpu(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {

        //判断操作类型
        if (eventType == CanalEntry.EventType.DELETE) {
            String spuId = "";
            List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
            for (CanalEntry.Column column : beforeColumnsList) {
                if (column.getName().equals("id")) {
                    spuId = column.getValue();//spuid
                    break;
                }
            }
            //todo 删除静态页

        }else{
            //新增 或者 更新
            List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
            String spuId = "";
            for (CanalEntry.Column column : afterColumnsList) {
                if (column.getName().equals("id")) {
                    spuId = column.getValue();
                    break;
                }
            }
            //更新 生成静态页
            pageFeign.createHtml(Long.valueOf(spuId));
        }
    }

    /**
    * @Description: 修改数据监听
    * @Param: [rowData]
    * @Return: void
    * @Author: Wangqibo
    * @Date: 2020/8/13/0013
    */
    @UpdateListenPoint
    public void onEventUpdate(CanalEntry.RowData rowData){
        System.out.println("UpdateListenPoint");
        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
        for (CanalEntry.Column column : afterColumnsList) {
            System.out.println("By--Annotation: " + column.getName() + " ::   " + column.getValue());
        }
    }
}
