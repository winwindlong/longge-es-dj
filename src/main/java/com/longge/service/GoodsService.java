package com.longge.service;

import com.longge.pojo.Goods;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class GoodsService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private GoodsRepository goodsRepository;

    public Boolean createIndexRest() {
        elasticsearchRestTemplate.createIndex(Goods.class);
        // 配置映射
        return this.elasticsearchRestTemplate.putMapping(Goods.class);
    }

    public Boolean existsById(long id) {
        return goodsRepository.existsById(id);
    }

    public Goods findById(long id) {
        Goods goods = goodsRepository.findById(id).orElse(null);
        return goods;
    }

    public Page<Goods> search(String keyword, int pageNo, int pageSize) {
        if (StringUtils.isEmpty(keyword)) {
            return null;
        }
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        TermQueryBuilder queryBuilder = QueryBuilders.termQuery("title", keyword);
        nativeSearchQueryBuilder.withQuery(queryBuilder);
        nativeSearchQueryBuilder.withPageable(PageRequest.of(pageNo, pageSize));
        AggregatedPage<Goods> page = (AggregatedPage<Goods>) this.goodsRepository.search(nativeSearchQueryBuilder.build());
        return page;
    }
}
