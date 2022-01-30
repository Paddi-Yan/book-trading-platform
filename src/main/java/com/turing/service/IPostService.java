package com.turing.service;

import com.turing.common.Result;
import com.turing.entity.Post;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qds
 * @since 2022-01-26
 */
public interface IPostService extends IService<Post> {
    Result getPostByCommunityId(Integer communityId);


}
