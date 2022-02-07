package com.turing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.turing.common.ActivityStatus;
import com.turing.common.HttpStatusCode;
import com.turing.common.RedisKey;
import com.turing.common.Result;
import com.turing.entity.Activity;
import com.turing.entity.QuestionAndAnswer;
import com.turing.entity.User;
import com.turing.entity.dto.ActivityDto;
import com.turing.mapper.ActivityMapper;
import com.turing.mapper.QAMapper;
import com.turing.mapper.UserMapper;
import com.turing.service.ActivityService;
import com.turing.utils.FTPUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月29日 13:44:24
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ActivityServiceImpl implements ActivityService
{

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private QAMapper qaMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Result addActivity(User user, ActivityDto activityDto, QuestionAndAnswer[] questionAndAnswers)
    {
        Activity activity = new Activity();
        activity.transform(activityDto);
        activity.setUserId(user.getId());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Timestamp timestamp = Timestamp.valueOf(simpleDateFormat.format(new Date()));
            activity.setStartTime(Timestamp.valueOf(simpleDateFormat.format(activityDto.getStartTime())));
            activity.setDeadline(Timestamp.valueOf(simpleDateFormat.format(activityDto.getDeadline())));
            activity.setCreatedTime(timestamp);
            log.info("{}-活动创建成功===>{}",timestamp,activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        activityMapper.insert(activity);
        for (QuestionAndAnswer questionAndAnswer : questionAndAnswers) {
            questionAndAnswer.setActivityId(activity.getId());
            qaMapper.insert(questionAndAnswer);
        }
        activityDto.transform(activity, Arrays.asList(questionAndAnswers),user );
        return new Result().success(activityDto);
    }

    /**
     * 添加/修改活动封面图片
     * @param file
     * @param id
     * @return
     */
    @Override
    public Result operateActivityCover(MultipartFile file, Long id)
    {
        Activity activity = activityMapper.selectById(id);
        if (activity == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("不存在该活动,操作失败!");
        }
        try {
            //先将新的封面进行保存
            String cover = FTPUtils.upload(file);
            //如果之前添加过封面 本次操作需要删除旧的封面图片
            if (activity.getCover()!=null)
            {
                FTPUtils.delete(activity.getCover());
            }
            activity.setCover(cover);
            activityMapper.updateById(activity);
            ActivityDto activityDto = buildActivityDto(activity);
            // 如果审核中 不存入缓存
            if (!ActivityStatus.EXAMINE.getStatus().equals(activityDto.getStatus()))
            {
                redisTemplate.opsForHash().put(RedisKey.ACTIVITY_HASH_KEY,RedisKey.ACTIVITY_HASH_FIELD+activityDto.getId(),activityDto);
            }
            log.info("活动信息更新成功===>>>{}",activityDto);
            return new Result().success(activityDto);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().fail(HttpStatusCode.ERROR).message("活动封面上传失败,请稍后重试！");
        }
    }

    @Override
    public Result getActivity()
    {
        Map result = redisTemplate.opsForHash().entries(RedisKey.ACTIVITY_HASH_KEY);
        if (result == null || result.isEmpty())
        {
            List<Activity> activityList = activityMapper.selectList(new QueryWrapper<Activity>().eq("status","1"));
            Map<String,ActivityDto> map = new HashMap<>();
            for (Activity activity : activityList) {
                ActivityDto activityDto = buildActivityDto(activity);
                map.put(RedisKey.ACTIVITY_HASH_FIELD+activity.getId(),activityDto);
            }
            redisTemplate.execute(new SessionCallback()
            {
                @Override
                public Object execute(RedisOperations operations) throws DataAccessException
                {
                    operations.opsForHash().putAll(RedisKey.ACTIVITY_HASH_KEY,map);
                    return null;
                }
            });
            result = map;
        }
        return new Result().success(result);
    }

    private ActivityDto buildActivityDto(Activity activity)
    {
        ActivityDto activityDto = new ActivityDto();
        User user = userMapper.selectById(activity.getUserId());
        List<QuestionAndAnswer> QAList = qaMapper.selectList(new QueryWrapper<QuestionAndAnswer>().eq("activity_id", activity.getId()));
        activityDto.transform(activity,QAList,user);
        return activityDto;
    }

    @Override
    public Result getActivityById(Long id)
    {
        ActivityDto activityDto = (ActivityDto) redisTemplate.opsForHash().get(RedisKey.ACTIVITY_HASH_KEY, RedisKey.TAG_HASH_FIELD + id);
        if (activityDto == null)
        {
            Activity activity = activityMapper.selectById(id);
            if (activity == null)
            {
                return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("不存在该活动!");
            }
            activityDto = buildActivityDto(activity);
            redisTemplate.opsForHash().put(RedisKey.ACTIVITY_HASH_KEY,RedisKey.ACTIVITY_HASH_FIELD+activity.getId(),activityDto);
        }
        return new Result().success(activityDto);
    }



    @Override
    public Result updateActivity(User user, ActivityDto activityDto, QuestionAndAnswer[] questionAndAnswers)
    {
        Activity activity = activityMapper.selectOne(new QueryWrapper<Activity>().eq("id",activityDto.getId()).eq("user_id",user.getId()));
        if (activity == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("不存在该活动信息,携带参数有误！");
        }
        String cover = activity.getCover();
        activityDto.setCover(cover);
        activity.transform(activityDto);
        //状态更新为审核中
        activity.setStatus(Byte.valueOf(String.valueOf(ActivityStatus.EXAMINE.getCode())));
        activityMapper.updateById(activity);
        qaMapper.delete(new QueryWrapper<QuestionAndAnswer>().eq("activity_id",activity.getId()));
        for (QuestionAndAnswer questionAndAnswer : questionAndAnswers) {
            questionAndAnswer.setActivityId(activity.getId());
            qaMapper.insert(questionAndAnswer);
        }
        List<QuestionAndAnswer> list = new ArrayList<>();
        Collections.addAll(list,questionAndAnswers);
        ActivityDto result = new ActivityDto();
        result.transform(activity,list,user);
        System.out.println(result.getTags());
        System.out.println(result.getCover());
        //状态更新为审核中需要将其从缓存移除
        redisTemplate.opsForHash().delete(RedisKey.ACTIVITY_HASH_KEY,RedisKey.ACTIVITY_HASH_FIELD+activity.getId());
        return new Result().success(result);
    }

    @Override
    public Result passExamine(Long id)
    {
        Activity activity = activityMapper.selectById(id);
        if (activity == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("不存在该活动信息,审核通过失败!");
        }
        activity.setStatus(Byte.valueOf(String.valueOf(ActivityStatus.EFFECTIVE.getCode())));
        //更新审核状态为有效
        activityMapper.updateById(activity);
        ActivityDto activityDto = buildActivityDto(activity);
        //加载到Redis缓存
        redisTemplate.opsForHash().put(RedisKey.ACTIVITY_HASH_KEY,RedisKey.ACTIVITY_HASH_FIELD+id,activityDto);
        log.info("活动[{}]通过审核,加载到缓存中!",activityDto);
        return new Result().success(activityDto);
    }

    @Override
    public Result withdraw(Long id)
    {
        Activity activity = activityMapper.selectById(id);
        if (activity == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("不存在该活动信息,审核通过失败!");
        }
        activity.setStatus(Byte.valueOf(String.valueOf(ActivityStatus.INVALID.getCode())));
        //更新审核状态为无效
        activityMapper.updateById(activity);
        //从缓存中删除
        redisTemplate.opsForHash().delete(RedisKey.ACTIVITY_HASH_KEY, RedisKey.ACTIVITY_HASH_FIELD + id);
        log.info("活动[{}]已下架,从Redis缓存删除中!",activity);
        return new Result().success(activity);
    }



}
