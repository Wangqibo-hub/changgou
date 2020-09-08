package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.search.pojo.SkuInfo;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-16 17:44
 * @description 自定义结果映射实现类
 */
@Component
public class SearchResultMapperImpl implements SearchResultMapper {
    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
        //1.获取数据
        List<T> content = new ArrayList<>();
        SearchHits hits = searchResponse.getHits();
        //2.获取总条数
        long total = hits.getTotalHits();
        //没有结果直接返回空值
        if (hits == null || total <= 0) {
            return new AggregatedPageImpl<>(content);
        }
        //设置高亮字段
        for (SearchHit hit : hits) {
            //获取结果，不高亮的
            String sourceAsString = hit.getSourceAsString();
            SkuInfo skuInfo = JSON.parseObject(sourceAsString, SkuInfo.class);
            //获取高亮的字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields != null
                    && highlightFields.size() > 0
                    && highlightFields.get("name") != null
                    && highlightFields.get("name").getFragments() != null) {
                Text[] fragments = highlightFields.get("name").fragments();
                StringBuffer stringBuffer = new StringBuffer();
                for (Text fragment : fragments) {
                    String string = fragment.string();
                    stringBuffer.append(string);
                }
                skuInfo.setName(stringBuffer.toString());
            }
            content.add((T) skuInfo);
        }
        //3.获取分类
        Aggregations aggregations = searchResponse.getAggregations();

        return new AggregatedPageImpl<T>(content, pageable, total, aggregations, searchResponse.getScrollId());
    }
}
