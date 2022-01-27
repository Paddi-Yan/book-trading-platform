package com.turing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.turing.common.Result;
import com.turing.entity.Community;
import com.turing.entity.CommunityInfor;
import com.turing.mapper.CommunityInforMapper;
import com.turing.mapper.CommunityMapper;
import com.turing.service.ICommunityService;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qds
 * @since 2022-01-26
 */
@Service
public class CommunityServiceImpl extends ServiceImpl<CommunityMapper, Community> implements ICommunityService {

    @Autowired
    CommunityInforMapper communityInforMapper;

    @Override
    public Result getCommunity(Integer userId) {
        List<CommunityInfor> communityByUserId = communityInforMapper.getCommunityByUserId(userId);
        return new Result().data(communityByUserId);
    }

    @Override
    public Result getCommunityInformation(Integer communityId) {
        CommunityInfor communityInfor = communityInforMapper.selectById(communityId);
        return new Result().data(communityInfor);
    }


}
