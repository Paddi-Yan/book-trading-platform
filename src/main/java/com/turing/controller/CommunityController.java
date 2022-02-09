package com.turing.controller;


import com.turing.common.HttpStatusCode;
import com.turing.common.Result;
import com.turing.entity.CommunityInfor;
import com.turing.interceptor.NoNeedToAuthorized;
import com.turing.service.HotService;
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
    @Autowired
    HotService hotService;


    @ResponseBody
    @ApiOperation("获取用户关注的社群")
    @PostMapping("/getCommunityByUser")
    @NoNeedToAuthorized
    public Result getCommunityByUser(Integer userId) {
        return communityService.getCommunity(userId);
    }

    @ResponseBody
    @ApiOperation("获取社群信息")
    @PostMapping("/getCommunityInformation")
    @NoNeedToAuthorized
    public Result getCommunityInformation(Integer communityId) {
        try {
            hotService.hotAdd(communityId,1);
        }catch (Exception e) {
            return communityService.getCommunityInformation(communityId).message("redis服务器未启动");
        }
        return communityService.getCommunityInformation(communityId);
    }

    @ResponseBody
    @ApiOperation("创建社区")
    @PostMapping("/createCommunityInformation")
    @NoNeedToAuthorized
    public Result createCommunityInformation(CommunityInfor communityInfor, MultipartFile photo) {

        if (communityInfor.getKind()>14 || communityInfor.getKind()<1){
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("社区类别输入异常");
        }

//        上传图片 返回图片地址
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


    @ResponseBody
    @ApiOperation("获取某一分类的社区,以图片中：言情为1，武侠为2，后面依次。热门和推荐单独接口")
    @PostMapping("/getCommunity")
    @NoNeedToAuthorized
    public Result getCommunity(Integer type) {
        if (type== null || type>14 || type<1){
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("社区类别输入异常");
        }
        return communityService.getCommunityByType(type);
    }

    @ResponseBody
    @ApiOperation("获取热门社区")
    @PostMapping("/getCommunityHot")
    @NoNeedToAuthorized
    public Result getCommunityHot() {
        return communityService.getCommunityHot();
    }

    @ResponseBody
    @ApiOperation("获取推荐社区")
    @PostMapping("/getCommunityRecommend")
    @NoNeedToAuthorized
    public Result getCommunityRecommend() {
        //推荐关注数前20的社区
        return communityService.getCommunityRecommend();
    }
}

