package com.turing.controller;

import com.turing.common.Result;
import com.turing.entity.dto.BookCommentDto;
import com.turing.interceptor.NoNeedToAuthorized;
import com.turing.service.BookCommentService;
import com.turing.utils.FTPUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年02月09日 17:00:54
 */
@RestController
@RequestMapping("/bookComment")
@Api(description = "书籍评论",tags = "BookCommentController")
public class BookCommentController
{
    @Autowired
    private BookCommentService commentService;

    @PostMapping(value = "/comment",headers = "content-type=multipart/form-data;")
    @ResponseBody
    @ApiOperation("用户评论书籍")
    public Result comment(@RequestParam(name = "files",required = false) MultipartFile[] files,BookCommentDto bookCommentDto)
    {
        if (files != null && files.length > 0)
        {
            ArrayList<String> photoList = new ArrayList<>();
            //上传评论图片
            for (MultipartFile file : files) {
                try {
                    String photo = FTPUtils.upload(file);
                    photoList.add(photo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            bookCommentDto.setPhoto(photoList);
        }
        return commentService.comment(bookCommentDto);
    }

    @GetMapping("/getCommentsByPage")
    @ResponseBody
    @ApiOperation("分页查询书籍的评论-不需要认证")
    @NoNeedToAuthorized
    public Result getCommentByPage(Integer page,Integer size,Integer bookId)
    {
        return commentService.getCommentByPage(page,size,bookId);
    }


}
