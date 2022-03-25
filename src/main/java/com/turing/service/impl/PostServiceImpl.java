package com.turing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.turing.common.HttpStatusCode;
import com.turing.common.Result;
import com.turing.entity.Comment;
import com.turing.entity.Post;
import com.turing.entity.User;
import com.turing.entity.dto.PostDto;
import com.turing.mapper.PostMapper;
import com.turing.service.IPostService;
import com.turing.service.LikeService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qds
 * @since 2022-01-26
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService {

    @Autowired
    PostMapper postMapper;
    @Autowired
    LikeService likeService;
    @Autowired
    CommentServiceImpl commentService;
    @Autowired
    UserServiceImpl userService;

    @Override
    public Result getPostByCommunityId(Integer communityId) {
        List<Post> postList = postMapper.getPostByCommunityId(communityId);
        List<PostDto> postDtoList = new ArrayList<>();
        Integer commentCount = 0;
        Integer likeCount= 0;


        for (Post post : postList) {
            PostDto postDto = new PostDto();
            int postId = post.getPostId().intValue();
            likeCount = (int)likeService.likeCount(Integer.valueOf(postId),1);

            QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("post_id", postId);
            commentCount = commentService.count(queryWrapper);

            User user = userService.getUserById(post.getUserId().intValue());
            String username = user.getUsername();
            String avatar = user.getAvatar();
            String address = "广东 湛江";

            postDto.transform(post,username,avatar,address,commentCount,likeCount);
            postDtoList.add(postDto);
        }

        return new Result().success(postDtoList);
    }

    @Override
    public Result sendPost(PostDto postDto) {
        Post post = new Post();
        post.transform(postDto);
        try {
            postMapper.insert(post);
        } catch (Exception e) {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR);
        }
        return new Result().success(null);
    }

}
