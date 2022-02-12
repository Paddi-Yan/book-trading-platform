package com.turing.controller;

import com.turing.common.HttpStatusCode;
import com.turing.common.Result;
import com.turing.interceptor.NoNeedToAuthorized;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年02月08日 17:33:07
 */
@RestController
@RequestMapping("/error")
public class ErrorController {
    @RequestMapping("/404")
    @NoNeedToAuthorized
    public Result notFoundError () {
        return new Result().fail(HttpStatusCode.NOT_FOUND);
    }

    @RequestMapping("/500")
    @NoNeedToAuthorized
    public Result serverError () {
        return new Result().fail(HttpStatusCode.ERROR);
    }

    @NoNeedToAuthorized
    public Result error () {
        return new Result().success("");
    }


}
