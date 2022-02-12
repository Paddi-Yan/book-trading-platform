package com.turing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.turing.common.Result;
import com.turing.entity.Comment;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qds
 * @since 2022-01-26
 */
public interface ICommentService extends IService<Comment> {

    Result getCommentByPostId (Integer postId);

    Result sentComment (Comment comment);

}
