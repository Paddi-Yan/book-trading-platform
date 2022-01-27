package com.turing.mapper;

import com.turing.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qds
 * @since 2022-01-26
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
