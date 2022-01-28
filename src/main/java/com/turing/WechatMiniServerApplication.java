package com.turing;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * @author 又蠢又笨的懒羊羊程序猿
 */
@SpringBootApplication
public class WechatMiniServerApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(WechatMiniServerApplication.class, args);

    }

}
