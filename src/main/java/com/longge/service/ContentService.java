package com.longge.service;

import com.alibaba.fastjson.JSON;
import com.longge.config.ESConstant;
import com.longge.pojo.Content;
import com.longge.pojo.Goods;
import com.longge.util.HtmlParseUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import javax.swing.text.Highlighter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class ContentService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;


    public Boolean parseContent(String keywords) throws Exception {
        List<Content> contentList = HtmlParseUtil.parseJD(keywords);
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m");
        for (Content content : contentList) {
            bulkRequest.add(new IndexRequest(ESConstant.JD_GOODS)
            .source(JSON.toJSONString(content), XContentType.JSON));
        }
        BulkResponse responses = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !responses.hasFailures();
    }

    public List<Map<String, Object>> searchPage(String keyword, Integer pageNo, Integer pageSize) throws IOException {
        if (Objects.isNull(pageSize) || pageSize < 1) {
            pageSize = 1;
        }
        SearchRequest searchRequest = new SearchRequest(ESConstant.JD_GOODS);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(pageNo);
        sourceBuilder.size(pageSize);
        //精确匹配
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", keyword);
        sourceBuilder.query(termQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        List<Map<String, Object>> resultMap = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField title = highlightFields.get("title");
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            if (Objects.nonNull(sourceAsMap)) {
                Text[] fragments = title.getFragments();
                String n_title = "";
                for (Text fragment : fragments) {
                    n_title += fragment;
                }
                sourceAsMap.put("title", n_title);
            }
            resultMap.add(sourceAsMap);
        }
        return resultMap;
    }
}
