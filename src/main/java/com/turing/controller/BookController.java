package com.turing.controller;

import com.turing.common.HttpStatusCode;
import com.turing.common.Result;
import com.turing.entity.dto.BookDto;
import com.turing.entity.User;
import com.turing.interceptor.NoNeedToAuthorized;
import com.turing.service.BookService;
import com.turing.service.UserService;
import com.turing.utils.FTPUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月21日 15:48:45
 */
@RestController
@RequestMapping("/book")
@Api(description = "书籍接口",tags = "BookController")
public class BookController
{
    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @PostMapping(value = "/upload",headers = "content-type=multipart/form-data;")
    @ResponseBody
    @ApiOperation("上传书籍信息")
    @NoNeedToAuthorized
    public Result uploadBookInfo(BookDto bookDto,@RequestParam(name = "files") MultipartFile[] files) throws ParseException
    {
        if (bookDto.getUserId() == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("用户ID不能为空");
        }
        User user = userService.getUserById(bookDto.getUserId());
        if (user == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("不存在该用户ID");
        }
        if (files != null)
        {
            List<String> bookList = new ArrayList<>();
            for (MultipartFile file : files) {
                //上传图片 返回图片地址
                String upload = null;
                try {
                    upload = FTPUtils.upload(file);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Result().fail(HttpStatusCode.ERROR).message(e.getMessage());
                }
                bookList.add(upload);
            }
            for (String s : bookList) {
                System.out.println(s);
            }
            bookDto.setPhotoList(bookList);
        }else
        {
            return new Result().fail(HttpStatusCode.ERROR).message("文件接收失败,无法上传!造成原因:Swagger不支持多文件上传,请用前端form表单或postman进行测试");
        }

        return bookService.uploadBookInfo(bookDto);
    }

    @ResponseBody
    @GetMapping("/getBookInfo")
    @ApiImplicitParam(name = "type",value = "类型:0-出书/1-求书",required = true)
    @ApiOperation("获取公共图书列表")
    @NoNeedToAuthorized
    public Result getAllBookInfo(Integer type)
    {
        return bookService.getBookInfo(type);
    }




    /*
    @PostMapping(value = "/fileUploadTest",headers = "content-type=multipart/form-data")
    @ResponseBody
    @ApiOperation("多文件上传测试")
    @NoNeedToAuthorized
    public Result fileUploadTest( MultipartFile[] files)
    {
        List<String> bookList = new ArrayList<>();
        if (files != null)
        {
            for (MultipartFile file : files) {
                //上传图片 返回图片地址
                String upload = null;
                try {
                    upload = FTPUtils.upload(file);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Result().fail(HttpStatusCode.ERROR).message(e.getMessage());
                }
                bookList.add(upload);
            }
            for (String s : bookList) {
                System.out.println(s);
            }
        } else
        {
            return new Result().fail(HttpStatusCode.ERROR).message("文件接收失败,无法上传!造成原因:Swagger不支持多文件上传,请用前端form表单或postman进行测试");
        }
        return new Result().success(bookList);
    }

    @PostMapping(value = "/oneFileUploadTest",headers = "content-type=multipart/form-data")
    @ResponseBody
    @ApiOperation("单文件上传测试")
    @NoNeedToAuthorized
    public Result oneFileUploadTest( MultipartFile file)
    {
        //上传图片 返回图片地址
        String upload = null;
        if (file != null)
        {

            try {
                upload = FTPUtils.upload(file);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result().fail(HttpStatusCode.ERROR).message(e.getMessage());
            }
        } else
        {
            return new Result().fail(HttpStatusCode.ERROR).message("文件接收失败,无法上传!");
        }
        return new Result().success(upload);
    }
*/

}
