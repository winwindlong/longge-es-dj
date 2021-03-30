package com.longge.service;

import com.longge.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {
}
