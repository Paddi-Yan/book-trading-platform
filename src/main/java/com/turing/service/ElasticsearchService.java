package com.turing.service;

import com.turing.common.Result;
import com.turing.entity.elasticsearch.RequestParams;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月28日 16:48:11
 */
public interface ElasticsearchService
{
    Result search(RequestParams requestParams);
}
