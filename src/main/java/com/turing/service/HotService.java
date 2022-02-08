package com.turing.service;

import com.turing.common.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class HotService {

    @Autowired
    private RedisTemplate redisTemplate;

    public void hotAdd(Integer communityId, int a) {
        try {
            String key = RedisKey.Hot + communityId;
            if (redisTemplate.hasKey(key)) {
                int old = (int) redisTemplate.opsForValue().get(key) + a;
                redisTemplate.opsForValue().set(key, old);
            } else {
                redisTemplate.opsForValue().set(key, a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Integer getHot(Integer communityId) {
        Integer a = 0;
        try {
            a = (Integer) redisTemplate.opsForValue().get(RedisKey.Hot + communityId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return a;
    }
}
