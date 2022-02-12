package com.turing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.turing.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qds
 * @since 2022-01-26
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    List<Comment> getCommentByPostId (Integer postId);

    List<Comment> getCommentByUserId (Integer userId);
}
