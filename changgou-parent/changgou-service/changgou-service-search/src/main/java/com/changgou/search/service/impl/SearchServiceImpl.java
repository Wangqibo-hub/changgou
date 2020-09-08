package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuEsMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchService;
import entity.Result;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-15 17:34
 * @description 搜索服务层接口实现类
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private SkuEsMapper skuEsMapper;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private SearchResultMapper searchResultMapper;

    @Override
    public void importToEs() {
        //调用goods微服务获取sku列表数据
        Result<List<Sku>> result = skuFeign.findByStatus("1");
        List<Sku> skuList = result.getData();
        //将sku转换成skuInfo
        List<SkuInfo> skuInfoList = JSON.parseArray(JSON.toJSONString(skuList), SkuInfo.class);
        //将spc字段映射到spcMap字段
        for (SkuInfo skuInfo : skuInfoList) {
            String spec = skuInfo.getSpec();
            Map map = JSON.parseObject(spec, Map.class);
            skuInfo.setSpecMap(map);
        }
        //将数据存入es
        skuEsMapper.saveAll(skuInfoList);
    }

    @Override
    public Map<String, Object> search(Map<String, Object> searchMap) {

        //1.创建查询对象的构建对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        //2.统计分类列表
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategoryGroup").field("categoryName").size(100));

        //3.统计品牌列表
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuBrandGroup").field("brandName").size(100));

        //4.统计规格列表
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuSpecGroup").field("spec.keyword").size(100));

        //5.设置筛选条件
        //5.1按关键字查询
        String keywords = (String) searchMap.get("keywords");
        //设置默认值
        if (StringUtils.isEmpty(keywords)) {
            keywords = "华为";
        }
        //5.2按分类筛选
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(searchMap.get("brand"))) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("brandName", searchMap.get("brand")));
        }
        //5.3按品牌筛选
        if (!StringUtils.isEmpty(searchMap.get("category"))) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("categoryName", searchMap.get("category")));
        }
        //5.4按规格筛选
        if (searchMap != null) {
            for (String key : searchMap.keySet()) {
                if (key.startsWith("spec_")) {
                    boolQueryBuilder.filter(QueryBuilders.termQuery("specMap." + key.substring(5) + ".keyword", searchMap.get(key)));
                }
            }
        }
        //5.5按价格筛选
        String price = (String) searchMap.get("price");
        if (!StringUtils.isEmpty(price)) {
            String[] strings = price.split("-");
            if (strings[1].equalsIgnoreCase("*")) {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(strings[0]));
            } else {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").from(strings[0], true).to(strings[1], true));
            }
        }
        nativeSearchQueryBuilder.withFilter(boolQueryBuilder);
        //5.6设置分页
        Integer pageNum = 1;
        String num = (String) searchMap.get("pageNum");
        if (!StringUtils.isEmpty(num)) {
            try {
                pageNum = Integer.valueOf(num);
            } catch (Exception e) {
                e.printStackTrace();
                pageNum = 1;
            }
        }
        Integer pageSize = 30;
        nativeSearchQueryBuilder.withPageable(PageRequest.of(pageNum - 1, pageSize));
        //5.7设置排序
        String sortFiled = (String) searchMap.get("sortField");
        String sortRole = (String) searchMap.get("sortRule");
        if (!StringUtils.isEmpty(sortFiled) && !StringUtils.isEmpty(sortRole)) {
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortFiled).order(sortRole.equalsIgnoreCase("DESC") ? SortOrder.DESC : SortOrder.ASC));
        }
        //5.8设置高亮
        nativeSearchQueryBuilder.withHighlightFields(new HighlightBuilder.Field("name"));
        nativeSearchQueryBuilder.withHighlightBuilder(new HighlightBuilder().preTags("<em style='color:red'>").postTags("</em>"));
        //扩大搜索范围
        nativeSearchQueryBuilder.withQuery(QueryBuilders.multiMatchQuery(keywords, "name", "brandName", "categoryName"));

        //6.构建查询对象
        NativeSearchQuery query = nativeSearchQueryBuilder.build();

        //7.执行查询
        AggregatedPage<SkuInfo> skuInfos = elasticsearchTemplate.queryForPage(query, SkuInfo.class, searchResultMapper);

        //8.获取查询结果
        //8.1获取分类列表
        StringTerms skuCategoryGroup = (StringTerms) skuInfos.getAggregation("skuCategoryGroup");
        List<String> categoryList = getTerms(skuCategoryGroup);
        //8.2获取品牌列表
        StringTerms skuBrandGroup = (StringTerms) skuInfos.getAggregation("skuBrandGroup");
        List<String> brandList = getTerms(skuBrandGroup);
        //8.3获取规格列表
        StringTerms skuSpecGroup = (StringTerms) skuInfos.getAggregation("skuSpecGroup");
        Map<String, Set<String>> specMap = getSpecMap(skuSpecGroup);
        //9.封装查询结果
        HashMap<String, Object> map = new HashMap<>();
        //9.1商品数据
        map.put("rows", skuInfos.getContent());
        //9.2总记录数
        map.put("total", skuInfos.getTotalElements());
        //9.3总页数
        map.put("totalPages", skuInfos.getTotalPages());
        //9.4分类列表
        map.put("categoryList",categoryList);
        //9.5品牌列表
        map.put("brandList", brandList);
        //9.6规格列表
        map.put("specMap", specMap);
        //9.7设置当前页码
        map.put("pageNum", pageNum);
        //9.8设置每页条数
        map.put("pageSize", pageSize);
        return map;
    }

    /**
    * @Description: 获取规格信息
    * @Param: [skuSpecGroup]
    * @Return: java.util.Map<java.lang.String,java.util.Set<java.lang.String>>
    * @Author: Wangqibo
    * @Date: 2020/8/16/0016
    */
    private Map<String, Set<String>> getSpecMap(StringTerms skuSpecGroup) {
        HashMap<String, Set<String>> specMap = new HashMap<>();
        Set<String> values = null;
        if (skuSpecGroup != null) {
            //获取查询结果的所有buckets
            List<StringTerms.Bucket> buckets = skuSpecGroup.getBuckets();
            for (StringTerms.Bucket bucket : buckets) {
                //获取每一个bucket的key（spec）
                String bucketKeyAsString = bucket.getKeyAsString();
                //将字符串的key转换成map（spec）
                Map<String, String> map = JSON.parseObject(bucketKeyAsString, Map.class);
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    //获取spec中的key
                    String key = entry.getKey();
                    //获取spec中的value
                    String value = entry.getValue();
                    //根据key获取返回结果中的set集合
                    values = specMap.get(key);
                    if (values == null) {
                        values = new HashSet<>();
                    }
                    //将value装入set中
                    values.add(value);
                    //将key，和对应的set集合装入返回结果中
                    specMap.put(key, values);
                }
            }
        }
        return specMap;
    }

    /**
    * @Description: 获取分组结果
    * @Param: [skuCategoryGroup]
    * @Return: java.util.List<java.lang.String>
    * @Author: Wangqibo
    * @Date: 2020/8/15/0015
    */
    private List<String> getTerms(StringTerms terms) {
        List<String> list = new ArrayList<>();
        if (terms != null) {
            List<StringTerms.Bucket> buckets = terms.getBuckets();
            for (StringTerms.Bucket bucket : buckets) {
                String key = bucket.getKeyAsString();
                list.add(key);
            }
        }
        return list;
    }
}
