package com.turing.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.turing.common.HttpStatusCode;
import com.turing.common.Result;
import com.turing.entity.elasticsearch.RequestParams;
import com.turing.service.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月28日 16:48:30
 */
@Service
@Slf4j
public class ElasticsearchServiceImpl implements ElasticsearchService
{
    @Autowired
    private RestHighLevelClient client;

    @Override
    public Result search(RequestParams params)
    {
        SearchRequest request = new SearchRequest(params.getType());
        buildBasicQuery(params,request);
        SearchResponse response = null;
        try {
             response = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("Elasticsearch查询失败===>>>{}",params);
            return new Result().fail(HttpStatusCode.ERROR).message("服务器内部出现错误,查询失败!");
        }
        return handleResponse(response);
    }

    private Result handleResponse(SearchResponse response)
    {
        long seconds = response.getTook().seconds();
        SearchHits hits = response.getHits();
        long total = hits.getTotalHits().value;
        log.info("Elasticsearch查询成功,耗时{}s,共{}条数据",seconds,total);
        Map<String,Object> result = new HashMap<>();
        List<String> data = new ArrayList<>();
        result.put("total",total);
        for (SearchHit hit : hits) {
            data.add(hit.getSourceAsString());
        }
        result.put("data",data);
        return new Result().success(result);
    }

    private void buildBasicQuery(RequestParams params, SearchRequest request)
    {
        String content = params.getContent();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 搜索内容
        if (StringUtils.isBlank(content))
        {
            queryBuilder.must(QueryBuilders.matchAllQuery());
        }else
        {
            queryBuilder.must(QueryBuilders.matchQuery("key",content));
        }

        queryBuilder.must(QueryBuilders.matchQuery("status",1));
        Integer minPrice = params.getMinPrice();
        Integer maxPrice = params.getMaxPrice();
        // 价格
        if (minPrice!=null && maxPrice!=null)
        {
            maxPrice = maxPrice.intValue() == 0 ? Integer.MAX_VALUE : maxPrice;
            queryBuilder.must(QueryBuilders.rangeQuery("price").gte(minPrice).lte(maxPrice));
        }
        request.source().query(queryBuilder);
    }
}
