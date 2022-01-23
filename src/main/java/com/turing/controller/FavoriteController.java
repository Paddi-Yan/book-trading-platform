package com.turing.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.turing.common.HttpStatusCode;
import com.turing.common.Result;
import com.turing.entity.Book;
import com.turing.entity.Favorite;
import com.turing.entity.User;
import com.turing.interceptor.NoNeedToAuthorized;
import com.turing.mapper.FavoriteMapper;
import com.turing.service.BookService;
import com.turing.service.FavoriteService;
import com.turing.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月21日 22:56:23
 */
@RestController
@RequestMapping("/favorite")
@Api(description = "用户收藏信息接口",tags = "FavoriteController")
public class FavoriteController
{
    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private FavoriteService favoriteService;

    @ResponseBody
    @ApiOperation("用户添加收藏")
    @PostMapping("/addFavorite")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "userId",value = "用户编号",required = true),
                    @ApiImplicitParam(name = "bookId",value = "书籍编号",required = true)
            }
    )
    public Result addFavorite(Integer userId,Integer bookId)
    {
        User user = userService.getUserById(userId);
        Book book = (Book) bookService.getBookInfoByBookId(bookId).getData();
        if (user == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("用户不存在,添加收藏失败!");
        }
        if (book == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("书籍编号不存在,添加收藏失败!");
        }
        return favoriteService.addFavorite(userId,book);
    }

    @ResponseBody
    @ApiOperation("获取用户收藏")
    @PostMapping("/getFavorites")
    @ApiImplicitParam(name = "userId",value = "用户编号",required = true)
    @NoNeedToAuthorized
    public Result getFavorites(Integer userId)
    {
        User user = userService.getUserById(userId);
        if (user == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("用户不存在,获取用户收藏失败!");
        }
        return favoriteService.getFavorites(userId);
    }

    @ResponseBody
    @ApiOperation("单个/批量删除用户收藏书籍")
    @PostMapping("/deleteFavorite")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "userId",value = "用户编号",required = true)
            }
    )
    @NoNeedToAuthorized
    public Result deleteFavorite(Integer userId,@RequestBody List<Integer> favoriteList)
    {
        User user = userService.getUserById(userId);
        if (user == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("用户不存在,获取用户收藏失败!");
        }
        return favoriteService.deleteFavorite(userId,favoriteList);
    }
}
