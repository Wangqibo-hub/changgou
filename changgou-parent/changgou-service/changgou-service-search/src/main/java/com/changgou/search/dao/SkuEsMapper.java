package com.changgou.search.dao;

import com.changgou.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-15 17:31
 * @description
 */
@Repository
public interface SkuEsMapper extends ElasticsearchRepository<SkuInfo,Long> {

}
