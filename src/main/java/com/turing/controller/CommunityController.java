package com.turing.controller;


import com.turing.common.Result;
import com.turing.interceptor.NoNeedToAuthorized;
import com.turing.service.ICommunityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qds
 * @since 2022-01-26
 */
@Controller
@RequestMapping("/community")
@Api(description = "社群相关接口", tags = "CommunityController")
public class CommunityController {
    @Autowired
    ICommunityService communityService;


    @ResponseBody
    @ApiOperation("获取用户关注的社群")
    @PostMapping("/getCommunity")
    @NoNeedToAuthorized
    public Result getCommunity(Integer userId) {
        return communityService.getCommunity(userId);
    }

    @ResponseBody
    @ApiOperation("获取社群信息")
    @PostMapping("/getCommunityInformation")
    @NoNeedToAuthorized
    public Result getCommunityInformation(Integer communityId) {
        return communityService.getCommunityInformation(communityId);

    }


}

