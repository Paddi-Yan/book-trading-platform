package com.turing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.turing.common.Result;
import com.turing.entity.Community;
import com.turing.entity.CommunityInfor;
import com.turing.entity.Post;
import com.turing.entity.dto.CommunityInforDto;
import com.turing.mapper.CommunityInforMapper;
import com.turing.mapper.CommunityMapper;
import com.turing.service.HotService;
import com.turing.service.ICommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qds
 * @since 2022-01-26
 */
@Service
public class CommunityServiceImpl extends ServiceImpl<CommunityInforMapper, CommunityInfor> implements ICommunityService {

    @Autowired
    CommunityInforMapper communityInforMapper;
    @Autowired
    CommunityMapper communityMapper;
    @Autowired
    PostServiceImpl postService;
    @Autowired
    HotService hotService;

    @Override
    public Result getCommunity(Integer userId) {
        List<CommunityInfor> communityByUserId = communityInforMapper.getCommunityByUserId(userId);
        return new Result().success(communityByUserId);
    }

    @Override
    public Result getCommunityInformation(Integer communityId) {
        CommunityInfor communityInfor = communityInforMapper.selectById(communityId);
        CommunityInforDto communityInforDto = new CommunityInforDto();
        Integer attention = 0;
        Integer topic = 0;
        Integer heat = 0;
        Integer sentCount = 0;

        QueryWrapper<Community> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("community_id", communityInfor.getComId());
        attention = communityMapper.selectCount(queryWrapper1);

        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("community_id", communityInfor.getComId());
        sentCount = postService.count(queryWrapper);

        heat = hotService.getHot(communityId);

        communityInforDto.transform(communityInfor,attention,topic,heat,sentCount);
        return new Result().success(communityInforDto);
    }

    @Override
    public Result createCommunity(CommunityInfor communityInfor) {
        communityInforMapper.insert(communityInfor);
        return new Result().success(null);
    }

    @Override
    public Result getCommunityByType(Integer type) {

        QueryWrapper<CommunityInfor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("kind", type);
        List<CommunityInfor> list = communityInforMapper.selectList(queryWrapper);
        List<CommunityInforDto> dtoList = new ArrayList<>();

        Integer attention = 0;
        Integer topic = 0;

        for (CommunityInfor communityInfor : list) {
            CommunityInforDto communityInforDto = new CommunityInforDto();

            QueryWrapper<Community> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("community_id", communityInfor.getComId());
            attention = communityMapper.selectCount(queryWrapper1);

            communityInforDto.transform(communityInfor, attention, topic);
            dtoList.add(communityInforDto);
        }

        return new Result().success(dtoList);

    }

    @Override
    public Result getCommunityRecommend() {
        List<CommunityInfor> list = list();
        List<CommunityInforDto> dtoList = new ArrayList<>();

        Integer attention = 0;
        Integer topic = 0;
        for (CommunityInfor communityInfor : list) {
            CommunityInforDto communityInforDto = new CommunityInforDto();

            QueryWrapper<Community> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("community_id", communityInfor.getComId());
            attention = communityMapper.selectCount(queryWrapper1);

            communityInforDto.transform(communityInfor, attention, topic);
            dtoList.add(communityInforDto);
        }

        Stream<CommunityInforDto> stream = dtoList.stream();
        List<CommunityInforDto> collect = stream.sorted((a, b) -> b.getAttention() - a.getAttention())
                .limit(20).collect(Collectors.toList());
        return new Result().success(collect);
    }


}
