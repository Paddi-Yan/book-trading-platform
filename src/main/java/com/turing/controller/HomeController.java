package com.turing.controller;

import com.turing.common.Result;
import com.turing.interceptor.NoNeedToAuthorized;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年02月07日 17:04:44
 */
@RestController
@RequestMapping("/home")
@Api(description = "首页展示信息", tags = "HomeController")
public class HomeController {


    @GetMapping("/get")
    @ApiOperation("获取首页展示数据")
    @ResponseBody
    @NoNeedToAuthorized
    public Result list () {
        return null;
    }
}
