package com.turing.controller;

import com.turing.common.HttpStatusCode;
import com.turing.common.Result;
import com.turing.entity.Tag;
import com.turing.entity.User;
import com.turing.interceptor.NoNeedToAuthorized;
import com.turing.service.TagService;
import com.turing.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月21日 22:11:26
 */
@RestController
@RequestMapping("/tag")
@Api(description = "书籍标签分类",tags = "TagsController")
public class TagsController
{
    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @GetMapping("/getAllTags")
    @ApiOperation("获取用户自定义书籍标签分类")
    public Result getAllTags(@RequestParam(required = true,name = "userId")Integer userId)
    {
        return tagService.getAllTags(userId);
    }


    @ResponseBody
    @GetMapping("/getPublicTags")
    @ApiOperation("获取书籍标签分类")
    @NoNeedToAuthorized
    public Result getPublicTags()
    {
        return tagService.getPublicTags();
    }

    @ResponseBody
    @PostMapping("/addTag")
    @ApiOperation("自定义分类标签")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "userId",value = "用户编号",required = true),
                    @ApiImplicitParam(name = "tagName",value = "标签分类名称",required = true)
            }
    )
    public Result addTag(Integer userId,String tagName)
    {
        User user = userService.getUserById(userId);
        if (user == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("该用户不存在,添加用户自定义标签失败!");
        }
        return tagService.addTag(userId,tagName);
    }

    @ResponseBody
    @DeleteMapping("/deleteTag")
    @ApiOperation("删除自定义分类标签")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "userId",value = "用户编号",required = true),
                    @ApiImplicitParam(name = "tagId",value = "标签分类编号",required = true)
            }
    )
    public Result deleteTag(Integer userId,Integer tagId)
    {
        User user = userService.getUserById(userId);
        if (user == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("该用户不存在,删除自定义标签失败!");
        }
        return tagService.deleteTag(userId,tagId);
    }

    @ResponseBody
    @PutMapping ("/editTag")
    @ApiOperation("编辑自定义分类标签")
    public Result editTag(@RequestBody Tag tag)
    {
        User user = userService.getUserById(tag.getUserId());
        if (user == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("该用户不存在,编辑自定义标签失败!");
        }
        return tagService.editTag(tag);
    }
}
