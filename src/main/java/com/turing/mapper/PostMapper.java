package com.turing.mapper;

import com.turing.entity.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qds
 * @since 2022-01-26
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {

    List<Post> getPostByCommunityId(Integer communityId);

    List<Post> getPostByUserId(Integer userId);

    List<Post> getPostByBookId(Integer bookId);

}
