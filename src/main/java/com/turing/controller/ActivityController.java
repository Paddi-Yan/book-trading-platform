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
    @GetMapping("/get")
    @ApiOperation("获取所有活动信息")
    @NoNeedToAuthorized
    public Result getActivity()
    {
        return activityService.getActivity();
    }

    @ResponseBody
    @GetMapping("/get/{id}")
    @ApiOperation("获取活动详情信息")
    @NoNeedToAuthorized
    public Result getActivityById(@PathVariable Long id)
    {
        return activityService.getActivityById(id);
    }

    @ResponseBody
    @PostMapping(value = "/add/{userId}")
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
    @PostMapping(value = "/operateCover/{id}",headers = "content-type=multipart/form-data;")
    @ApiOperation("添加/修改活动封面图片")
    public Result operateActivityCover(@RequestParam MultipartFile file,@PathVariable(name = "id",required = true)Long id)
    {
        return activityService.operateActivityCover(file,id);
    }

    @ResponseBody
    @PutMapping(value = "/update/{userId}/{id}")
    @ApiOperation("修改活动信息")
    public Result updateActivity(@PathVariable Long userId,
                                 @PathVariable Long id,
                                 ActivityDto activityDto,
                                 @RequestBody QuestionAndAnswer[] questionAndAnswers)
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
        if (questionAndAnswers==null || questionAndAnswers.length == 0)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("常见问答信息不得为空！");
        }
        activityDto.setId(id);
        return activityService.updateActivity(user,activityDto,questionAndAnswers);
    }

    @ResponseBody
    @PostMapping("/passExamine/{id}")
    @ApiOperation("通过活动审核")
    @NoNeedToAuthorized
    public Result passExamine(@PathVariable Long id)
    {
        return activityService.passExamine(id);
    }


    @ResponseBody
    @DeleteMapping("/withdraw/{id}")
    @ApiOperation("下架活动")
    @NoNeedToAuthorized
    public Result withdraw(@PathVariable Long id)
    {
        return activityService.withdraw(id);
    }
}
