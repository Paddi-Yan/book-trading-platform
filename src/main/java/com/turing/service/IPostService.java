package com.turing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.turing.common.Result;
import com.turing.entity.Post;
import com.turing.entity.dto.PostDto;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qds
 * @since 2022-01-26
 */
public interface IPostService extends IService<Post> {
    Result getPostByCommunityId (Integer communityId);

    Result sendPost (PostDto postDto);


}
