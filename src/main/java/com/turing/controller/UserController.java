package com.turing.controller;

import com.turing.common.HttpStatusCode;
import com.turing.common.Result;
import com.turing.entity.Book;
import com.turing.entity.User;
import com.turing.entity.dto.BookDto;
import com.turing.interceptor.NoNeedToAuthorized;
import com.turing.service.BookService;
import com.turing.service.UserService;
import com.turing.utils.FTPUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月20日 01:11:52
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api(description = "用户信息接口",tags = "UserController")
public class UserController
{

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @ResponseBody
    @GetMapping("/getUserInfo")
    @ApiOperation("获取用户信息")
    @ApiImplicitParam(name = "refreshToken",value = "是否刷新令牌有效时间",required = true)
    public Result getUserInfo(Boolean refreshToken)
    {
        return userService.getUserInfo(refreshToken);
    }

    @ResponseBody
    @GetMapping("/getBookInfo")
    @ApiOperation("获取用户的图书列表")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "type",value = "类型:0-出书/1-求书",required = true),
                    @ApiImplicitParam(name = "userId",value = "用户ID",required = true)
            }
    )
    public Result getBookInfo(Integer type,Integer userId)
    {
        return userService.getBookInfo(userId,type);
    }

    @ResponseBody
    @PutMapping ("/updateBookInfo")
    @ApiOperation("用户修改图书信息")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "changePhoto",value = "是否有修改图片信息 0-否/1-是",required = true),
                    @ApiImplicitParam(name = "id",value = "书籍编号",required = true,type = "int")
            }
    )
    public Result updateBookInfo(BookDto bookDto, MultipartFile[] files,Integer changePhoto)
    {
        User user = userService.getUserById(bookDto.getUserId());
        if (user == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("用户ID不存在,修改图书信息失败!");
        }
        if (changePhoto == 1)
        {
            //改变了图片信息,删除原有的图片信息,上传新的图片信息到服务器
            Book book = (Book) bookService.getBookInfoByBookId(bookDto.getId()).getData();
            for (String photo : book.getPhoto().split(",")) {
                System.out.println(photo);
                try {
                    FTPUtils.delete(photo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            List photoList = new ArrayList();
            for (MultipartFile file : files) {
                String upload = null;
                try {
                    //返回图片URL
                    upload = FTPUtils.upload(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                photoList.add(upload);
            }
            bookDto.setPhotoList(photoList);
        }
        return userService.updateBookInfo(bookDto);
    }

    @ResponseBody
    @DeleteMapping("/withdrawBookInfo")
    @ApiOperation("用户下架图书信息")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "bookId",value = "图书编号",required = true),
                    @ApiImplicitParam(name = "userId",value = "用户编号",required = true)
            }
    )
    public Result withdrawBookInfo(Integer bookId,Integer userId)
    {
        User user = userService.getUserById(userId);
        if (user == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("不存在该用户ID");
        }
        return userService.withdrawBookInfo(bookId,userId);
    }

    @ResponseBody
    @DeleteMapping("/deleteHistory")
    @ApiOperation("用户删除已下架的图书信息")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "bookId",value = "图书编号",required = true),
                    @ApiImplicitParam(name = "userId",value = "用户编号",required = true)
            }
    )
    public Result deleteHistory(Integer bookId,Integer userId)
    {
        User user = userService.getUserById(userId);
        if (user == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("不存在该用户ID");
        }
        Book book = (Book) bookService.getBookInfoByBookId(bookId).getData();
        BookDto bookDto = new BookDto();
        bookDto.transform(book);
        for (String photo : bookDto.getPhotoList()) {
            try {
                FTPUtils.delete(photo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userService.deleteHistory(bookId,userId);
    }


}
