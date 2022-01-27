package com.turing.service.impl;

import com.turing.common.Result;
import com.turing.entity.Comment;
import com.turing.mapper.CommentMapper;
import com.turing.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public Result getCommentByPostId(Integer postId) {
        return new Result().success(commentMapper.getCommentByPostId(postId));
    }
}
