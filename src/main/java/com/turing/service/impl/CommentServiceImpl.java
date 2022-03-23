package com.turing.service.impl;

import com.turing.common.HttpStatusCode;
import com.turing.common.Result;
import com.turing.entity.Comment;
import com.turing.entity.User;
import com.turing.entity.dto.CommentDto;
import com.turing.mapper.CommentMapper;
import com.turing.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.turing.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qds
 * @since 2022-01-26
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    LikeService likeService;

    @Autowired
    UserServiceImpl userService;

    @Override
    public Result getCommentByPostId(Integer postId) {
        List<Comment> list = commentMapper.getCommentByPostId(postId);
        List<CommentDto> collect = list.stream().map(new Function<Comment, CommentDto>() {
            @Override
            public CommentDto apply(Comment comment) {
                CommentDto commentDto = new CommentDto();
                long count = likeService.likeCount(comment.getId().intValue(), 2);
                User user = userService.getUserById(comment.getUserId().intValue());
                String username = user.getUsername();
                String avatar = user.getAvatar();
                commentDto.transform(comment, username, avatar, count);
                return commentDto;
            }
        }).collect(Collectors.toList());
        return new Result().success(collect);
    }

    @Override
    public Result sentComment(Comment comment) {
        try {
            commentMapper.insert(comment);
        }catch (Exception e){
               return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR);
        }
        return new Result().success(null);
    }
}
