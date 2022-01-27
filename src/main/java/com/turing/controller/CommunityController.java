package com.turing.controller;


import com.turing.common.Result;
import com.turing.entity.CommunityInfor;
import com.turing.interceptor.NoNeedToAuthorized;
import com.turing.service.ICommentService;
import com.turing.service.ICommunityService;
import com.turing.service.IPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author qds
 * @since 2022-01-26
 */
@RestController
@RequestMapping("/community")
@Api(description = "社群相关接口",tags = "CommunityController")
public class CommunityController {
    @Autowired
    ICommunityService communityService;

    @Autowired
    IPostService postService;

    @ResponseBody
    @ApiOperation("获取用户关注的社群")
    @PostMapping("/getCommunity")
    @NoNeedToAuthorized
    public Result getCommunity(Integer userId){
        return communityService.getCommunity(userId);
    }

    @ResponseBody
    @ApiOperation("获取社群信息")
    @PostMapping("/getCommunityInformation")
    @NoNeedToAuthorized
    public Result getCommunityInformation(Integer communityId){
        return communityService.getCommunityInformation(communityId);

    }

    @ResponseBody
    @ApiOperation("获取社群所有帖子")
    @PostMapping("/getPostByCommunityId")
    @NoNeedToAuthorized
    public Result getPostByCommunityId(Integer communityId){
        return postService.getPostByCommunityId(communityId);
    }

}

