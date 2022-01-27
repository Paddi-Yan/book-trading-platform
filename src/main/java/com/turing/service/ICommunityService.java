package com.turing.service;

import com.turing.common.Result;
import com.turing.entity.Community;
import com.baomidou.mybatisplus.extension.service.IService;
import com.turing.mapper.CommunityMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qds
 * @since 2022-01-26
 */
public interface ICommunityService extends IService<Community> {
    Result getCommunity(Integer userId);

    public Result getCommunityInformation(Integer communityId);

}
