package com.turing.config;

import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月28日 14:36:00
 */
@Configuration
@Data
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration
{

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private Integer port;

    @Override
    public RestHighLevelClient elasticsearchClient()
    {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(host,port,"http"))
        );
    }
}
