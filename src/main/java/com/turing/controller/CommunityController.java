package com.turing.controller;


import com.turing.common.HttpStatusCode;
import com.turing.common.Result;
import com.turing.entity.CommunityInfor;
import com.turing.interceptor.NoNeedToAuthorized;
import com.turing.service.ICommunityService;
import com.turing.utils.FTPUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qds
 * @since 2022-01-26
 */
@Controller
@RequestMapping("/community")
@Api(description = "社群相关接口", tags = "CommunityController")
public class CommunityController {
    @Autowired
    ICommunityService communityService;


    @ResponseBody
    @ApiOperation("获取用户关注的社群")
    @PostMapping("/getCommunity")
    @NoNeedToAuthorized
    public Result getCommunity(Integer userId) {
        return communityService.getCommunity(userId);
    }

    @ResponseBody
    @ApiOperation("获取社群信息")
    @PostMapping("/getCommunityInformation")
    @NoNeedToAuthorized
    public Result getCommunityInformation(Integer communityId) {
        return communityService.getCommunityInformation(communityId);

    }

    @ResponseBody
    @ApiOperation("创建社区")
    @PostMapping("/createCommunityInformation")
    @NoNeedToAuthorized
    public Result createCommunityInformation(CommunityInfor communityInfor, MultipartFile photo) {

        //上传图片 返回图片地址
        String upload = null;
        if (photo != null) {
            try {
                upload = FTPUtils.upload(photo);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result().fail(HttpStatusCode.ERROR).message(e.getMessage());
            }
        } else {
            return new Result().fail(HttpStatusCode.ERROR).message("文件接收失败,无法上传!");
        }
        communityInfor.setComPhoto(upload);

        return communityService.createCommunity(communityInfor);

    }


}

