package com.turing.service;

import com.turing.common.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class HotService {

    @Autowired
    private RedisTemplate redisTemplate;

    public void hotAdd (Integer communityId, int a) {
        String key = RedisKey.Hot + communityId;
        if (redisTemplate.hasKey(key)) {
            int old = (int) redisTemplate.opsForValue().get(key) + a;
            System.out.println(old);
            redisTemplate.opsForValue().set(key, old);
        } else {
            redisTemplate.opsForValue().set(key, a);
        }
    }

    public Integer getHot (Integer communityId) {
        return (Integer) redisTemplate.opsForValue().get(RedisKey.Hot + communityId);
    }
}
