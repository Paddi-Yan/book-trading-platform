package com.turing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.turing.common.Result;
import com.turing.entity.CommunityInfor;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qds
 * @since 2022-01-26
 */
public interface ICommunityService extends IService<CommunityInfor> {
    Result getCommunity(Integer userId);

    Result getCommunityInformation(Integer communityId);

    Result createCommunity(CommunityInfor communityInfor);

    Result getCommunityByType(Integer type);

    Result getCommunityRecommend();

    Result getCommunityHot();

}
