package com.turing.controller;

import com.turing.common.ElasticsearchIndex;
import com.turing.common.HttpStatusCode;
import com.turing.common.Result;
import com.turing.entity.elasticsearch.RequestParams;
import com.turing.service.ElasticsearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月28日 16:46:50
 */
@RestController
@RequestMapping("/search")
@Api(value = "搜索书籍/用户/社区接口",tags = "SearchController")
@Slf4j
public class SearchController
{
    @Autowired
    private ElasticsearchService elasticsearchService;

    @PostMapping("/search")
    @ApiOperation("搜索获取信息")
    @ResponseBody
    private Result search(@RequestBody RequestParams params)
    {
        if (params.getType() == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("必须携带参数[key]指定搜索类型");
        }
        String key = params.getType();
        if (key != ElasticsearchIndex.BOOK && key!= ElasticsearchIndex.USER && key != ElasticsearchIndex.COMMUNITY)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("携带参数[key]错误! <书籍:book 用户:user 社群:community>");
        }
        return elasticsearchService.search(params);
    }
}
