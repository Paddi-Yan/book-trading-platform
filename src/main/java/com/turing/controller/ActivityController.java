package com.turing.controller;

import com.turing.common.HttpStatusCode;
import com.turing.common.Result;
import com.turing.entity.QuestionAndAnswer;
import com.turing.entity.User;
import com.turing.entity.dto.ActivityDto;
import com.turing.interceptor.NoNeedToAuthorized;
import com.turing.service.ActivityService;
import com.turing.service.UserService;
import com.turing.utils.FTPUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月29日 13:43:13
 */
@RestController
@RequestMapping("/activity")
@Slf4j
@Api(description = "活动信息",tags = "ActivityController")
public class ActivityController
{
    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @GetMapping("/getActivity")
    @ApiOperation("获取所有活动信息")
    @ApiImplicitParam(name = "refresh",value = "是否需要刷新缓存数据 不传参-默认不需要 1-需要进行缓存刷新",required = false)
    public Result getActivity(Integer refresh)
    {
        return activityService.getActivity(refresh);
    }

    @ResponseBody
    @PostMapping(value = "/addActivity/{userId}")
    @ApiOperation("添加活动信息")
    public Result addActivity(ActivityDto activityDto,
                              @RequestBody QuestionAndAnswer[] questionAndAnswers,
                              @PathVariable(name = "userId",required = true)Long userId)
    {
        if (activityDto.getStartTime().after(activityDto.getDeadline()))
        {
            return new Result().fail(HttpStatusCode.ERROR).message("活动截止日期与活动开始日期参数错误!");
        }
        User user = userService.getUserById(Math.toIntExact(userId));
        if (user == null)
        {
            return new Result().fail(HttpStatusCode.ERROR).message("不存在该用户!");
        }
        return activityService.addActivity(user,activityDto,questionAndAnswers);
    }

    @ResponseBody
    @PostMapping(value = "/operateActivityCover/{id}",headers = "content-type=multipart/form-data;")
    @ApiOperation("添加/修改活动封面图片")
    public Result operateActivityCover(@RequestParam MultipartFile file,@PathVariable(name = "id",required = true)Long id)
    {
        return activityService.operateActivityCover(file,id);
    }


}
