package com.turing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.turing.common.Result;
import com.turing.entity.Post;
import com.turing.mapper.PostMapper;
import com.turing.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public Result getPostByCommunityId(Integer communityId) {
        return new Result().success(postMapper.getPostByCommunityId(communityId));
    }

}
