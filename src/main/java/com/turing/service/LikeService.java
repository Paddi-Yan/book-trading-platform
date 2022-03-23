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
     *
     * @param kindId
     * @param userId
     * @param kind 1为帖子，2为评论，3为二级评论
     */
    public void like(Integer kindId, Integer userId,int kind) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String entityLikeKey = null;
                if (kind == 1){
                    entityLikeKey = RedisKey.LIKE_POST + kindId;
                }else if (kind == 2){
                    entityLikeKey = RedisKey.LIKE_COMMENT + kindId;
                }

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
    public long likeCount(Integer kindId ,int kind) {
        String entityLikeKey = null;
        if (kind == 1){
            entityLikeKey = RedisKey.LIKE_POST + kindId;
        }else if (kind == 2){
            entityLikeKey = RedisKey.LIKE_COMMENT + kindId;
        }
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    /**
     * 查询某个用户对某个实体的点赞状态（是否已赞）
     *
     * @return 1:已赞，0:未赞
     */
    public int userLikeStatus(Integer userId, Integer kindId,int kind) {
        String entityLikeKey = null;
        if (kind == 1){
            entityLikeKey = RedisKey.LIKE_POST + kindId;
        }else if (kind == 2){
            entityLikeKey = RedisKey.LIKE_COMMENT + kindId;
        }
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }

}