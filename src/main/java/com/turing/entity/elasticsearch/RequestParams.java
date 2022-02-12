package com.turing.entity.elasticsearch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月28日 14:42:24
 */
@Data
public class RequestParams {
    @ApiModelProperty(required = true, name = "查询类型", value = "书籍:book 用户:user 社群:community")
    private String type;
    @ApiModelProperty(name = "content", value = "搜索内容:书籍名称/用户名称/社群名称")
    private String content;
    @ApiModelProperty(name = "书籍最小价格")
    private Integer minPrice;
    @ApiModelProperty(name = "书籍最大价格")
    private Integer maxPrice;
}
