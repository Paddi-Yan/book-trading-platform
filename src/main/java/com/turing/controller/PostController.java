package com.turing.controller;


import com.turing.common.Result;
import com.turing.interceptor.NoNeedToAuthorized;
import com.turing.service.ICommentService;
import com.turing.service.IPostService;
import com.turing.service.LikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qds
 * @since 2022-01-26
 */
@Controller
@RequestMapping("/post")
@Api(description = "帖子相关接口", tags = "PostController")
public class PostController {

    @Autowired
    IPostService postService;

    @Autowired
    ICommentService commentService;

    @Autowired
    LikeService likeService;

    @ResponseBody
    @ApiOperation("获取社群所有帖子")
    @PostMapping("/getPostByCommunityId")
    @NoNeedToAuthorized
    public Result getPostByCommunityId(Integer communityId) {
        return postService.getPostByCommunityId(communityId);
    }

    @ResponseBody
    @ApiOperation("获取帖子所有评论")
    @PostMapping("/getCommentByPostId")
    @NoNeedToAuthorized
    public Result getCommentByPostId(Integer postId) {
        return commentService.getCommentByPostId(postId);
    }

    @ResponseBody
    @ApiOperation("点赞")
    @PostMapping("/like")
    @NoNeedToAuthorized
    public Result like(Integer postId, Integer userId) {
        likeService.like(postId, userId);
        long count = likeService.likeCount(postId);
        int status = likeService.userLikeStatus(userId, postId);
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", count);
        map.put("likeStatus", status);
        return new Result().success(map);
    }

}

