package com.turing.controller;


import com.turing.common.HttpStatusCode;
import com.turing.common.Result;
import com.turing.entity.Comment;
import com.turing.entity.Post;
import com.turing.entity.dto.PostDto;
import com.turing.interceptor.NoNeedToAuthorized;
import com.turing.service.HotService;
import com.turing.service.ICommentService;
import com.turing.service.IPostService;
import com.turing.service.LikeService;
import com.turing.utils.FTPUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    @Autowired
    HotService hotService;

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
        Post post = postService.getById(postId);
        if (post == null) return new Result().success(null);
        hotService.hotAdd(Math.toIntExact(post.getCommunityId()), 1);
        return commentService.getCommentByPostId(postId);
    }

    @ResponseBody
    @ApiOperation("发评论")
    @PostMapping("/sendComment")
    @NoNeedToAuthorized
    public Result sendComment(Comment comment) {
        Post post = postService.getById(comment.getPostId());
        if (comment.getContent() == null || comment.getContent().isEmpty() ||comment.getPostId() ==null||comment.getUserId() ==null || post == null) return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR);
        hotService.hotAdd(Math.toIntExact(post.getCommunityId()), 5);
        return commentService.sentComment(comment);
    }

    @ResponseBody
    @ApiOperation("发帖子")
    @PostMapping("/sendPost")
    @NoNeedToAuthorized
    public Result sendPost(PostDto postDto, MultipartFile[] photos) {
        if (postDto.getCommunityId() == null || postDto.getUserId() == null||postDto.getTitle() == null) {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR);
        }
        if (photos != null) {
            List<String> List = new ArrayList<>();
            for (MultipartFile file : photos) {
                //上传图片 返回图片地址
                String upload = null;
                try {
                    upload = FTPUtils.upload(file);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Result().fail(HttpStatusCode.ERROR).message(e.getMessage());
                }
                List.add(upload);
            }
            postDto.setPhotoList(List);
        }
        hotService.hotAdd(Math.toIntExact(postDto.getCommunityId()), 5);
        return postService.sendPost(postDto);
    }

    @ResponseBody
    @ApiOperation("帖子点赞")
    @PostMapping("/like")
    @NoNeedToAuthorized
    public Result like(Integer postId, Integer userId) {
        if (postId == null || userId == null) return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR);
        likeService.like(postId, userId,1);
        long count = likeService.likeCount(postId,1);
        int status = likeService.userLikeStatus(userId, postId,1);
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", count);
        map.put("likeStatus", status);
        return new Result().success(map);
    }

}

