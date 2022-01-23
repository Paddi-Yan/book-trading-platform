package com.turing.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月19日 23:46:38
 */
@Data
public class Result
{
    private Object data;
    private String message;
    private Integer code;

    public Result success(Object data)
    {
        return this.message(HttpStatusCode.SUCCESS.getMessage())
                .code(HttpStatusCode.SUCCESS.getCode())
                .data(data);
    }

    public Result fail(HttpStatusCode httpStatusCode)
    {
        return this.message(httpStatusCode.getMessage())
                .code(httpStatusCode.getCode())
                .data("无返回数据");
    }


    public Result message(String message)
    {
        this.message = message;
        return this;
    }

    private Result code(Integer code)
    {
        this.code = code;
        return this;
    }

    public Result data(Object data)
    {
        this.data = data;
        return this;
    }

}
