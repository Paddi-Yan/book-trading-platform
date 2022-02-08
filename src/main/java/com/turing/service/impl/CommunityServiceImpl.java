package com.turing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.turing.common.HttpStatusCode;
import com.turing.common.RedisKey;
import com.turing.common.Result;
import com.turing.entity.Community;
import com.turing.entity.CommunityInfor;
import com.turing.entity.Post;
import com.turing.entity.User;
import com.turing.entity.dto.CommunityInforDto;
import com.turing.mapper.CommunityInforMapper;
import com.turing.mapper.CommunityMapper;
import com.turing.mapper.UserMapper;
import com.turing.service.HotService;
import com.turing.service.ICommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
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
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserMapper userMapper;

    @Override
    public Result getCommunity(Integer userId) {
        List<CommunityInfor> communityByUserId = communityInforMapper.getCommunityByUserId(userId);
        try {
            User user = userMapper.selectById(userId);
            if (user==null){
                return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("用户不存在");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result().fail(HttpStatusCode.ERROR).data(communityByUserId);
        }
        return new Result().success(communityByUserId);
    }

    @Override
    public Result getCommunityInformation(Integer communityId) {
        CommunityInfor communityInfor = communityInforMapper.selectById(communityId);
        if (communityInfor == null){
            return new Result().success(null);
        }
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
        try {
            communityInforMapper.insert(communityInfor);
        }catch (Exception e){
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("用户不存在，或社区名称不能为空");
        }
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

    @Override
    public Result getCommunityHot() {
        Set keys = redisTemplate.keys(RedisKey.Hot);
        System.out.println(keys.toString());
        Map<Integer,Integer> map = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2-o1;
            }
        });
        for (Object key : keys) {
            Integer hotCount = (Integer)redisTemplate.opsForValue().get(key);
            Integer comId = Integer.valueOf(((String)key).substring(4));
            map.put(comId,hotCount);
        }
        List<Integer> collect = map.entrySet().stream()
                .sorted((o1, o2) -> o2.getValue() - o1.getValue()).limit(20)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        System.out.println(collect);
        QueryWrapper<CommunityInfor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("com_id", collect);
        List<CommunityInfor> list = communityInforMapper.selectList(queryWrapper);
        return new Result().success(list);
    }


}
