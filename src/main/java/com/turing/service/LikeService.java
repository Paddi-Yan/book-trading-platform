package com.turing.service;

import com.turing.common.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Key(like_post_{postId}) : Value({userId})
     *
     * @param postId
     * @param userId
     */
    public void like (Integer postId, Integer userId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute (RedisOperations redisOperations) throws DataAccessException {
                String entityLikeKey = RedisKey.LIKE_POST + postId;

                // 判断用户是否已经点过赞了
                boolean isMember = redisOperations.opsForSet().isMember(entityLikeKey, userId);

                redisOperations.multi(); // 开启事务

                if (isMember) {
                    // 如果用户已经点过赞，点第二次则取消赞
                    redisOperations.opsForSet().remove(entityLikeKey, userId);
                } else {
                    redisTemplate.opsForSet().add(entityLikeKey, userId);
                }

                return redisOperations.exec(); // 提交事务
            }
        });
    }

    /**
     * 查询某实体被点赞的数量
     */
    public long likeCount (Integer postId) {
        String entityLikeKey = RedisKey.LIKE_POST + postId;
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    /**
     * 查询某个用户对某个实体的点赞状态（是否已赞）
     *
     * @return 1:已赞，0:未赞
     */
    public int userLikeStatus (Integer userId, Integer postId) {
        String entityLikeKey = RedisKey.LIKE_POST + postId;
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }

}