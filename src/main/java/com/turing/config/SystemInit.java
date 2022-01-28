package com.turing.config;

import com.turing.common.ElasticsearchIndex;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月28日 15:34:29
 */
@Component
@Slf4j
public class SystemInit
{
    @Autowired
    private RestHighLevelClient client;

    @PostConstruct
    public void initElasticSearch()
    {
        log.info("========================ES初始化开始==========================");
        try {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(ElasticsearchIndex.BOOK);
            arrayList.add(ElasticsearchIndex.USER);
            for (String index : arrayList) {
                GetIndexRequest getIndexRequest = new GetIndexRequest(index);
                Boolean exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
                log.info(index+"索引是否存在:"+exists);
                if (!exists)
                {
                    CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
                    switch (index){
                        case ElasticsearchIndex.BOOK:
                            createIndexRequest.source(ElasticsearchIndex.BOOK_INDEX, XContentType.JSON);
                            break;
                        case ElasticsearchIndex.USER:
                            createIndexRequest.source(ElasticsearchIndex.USER_INDEX,XContentType.JSON);
                            break;
                    }
                    client.indices().create(createIndexRequest,RequestOptions.DEFAULT);
                }
            }
            log.info("========================ES初始化完毕==========================");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
