package com.turing.controller;

import com.turing.common.HttpStatusCode;
import com.turing.common.Result;
import com.turing.entity.User;
import com.turing.service.BookService;
import com.turing.service.CartService;
import com.turing.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月30日 11:52:15
 */
@RestController
@RequestMapping("/cart")
@Api(description = "购物车管理", tags = "CartController")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @ResponseBody
    @PostMapping("/add/{bookId}")
    @ApiOperation(value = "添加购物车")
    public Result addCart (@PathVariable Integer bookId, @RequestParam Long userId, @RequestParam Integer count) {
        User user = userService.getUserById(Math.toIntExact(userId));
        if (user == null) {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("不存在该用户!");
        }
        if (count == null || count <= 0) {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("物品数量非法!");
        }
        return cartService.addCart(user, bookId, count);
    }

    @ResponseBody
    @GetMapping("/get/{userId}")
    @ApiOperation(value = "获取用户的购物车")
    @ApiImplicitParam(name = "refresh", value = "是否需要刷新缓存数据 不传参-默认不需要 1-需要进行缓存刷新", required = false)
    public Result getCart (@PathVariable Long userId, Integer refresh) {
        if (checkUserInfo(userId)) {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("不存在该用户!");
        }
        return cartService.getCart(userId, refresh);
    }


    @ResponseBody
    @DeleteMapping("/delete/{userId}")
    @ApiOperation("单个/批量删除购物车物品")
    public Result deleteCart (@PathVariable Long userId, @RequestBody List<Long> cartIdList) {
        if (checkUserInfo(userId)) {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("不存在该用户!");
        }
        return cartService.deleteCart(userId, cartIdList);
    }

    private boolean checkUserInfo (@PathVariable Long userId) {
        User user = userService.getUserById(Math.toIntExact(userId));
        if (user == null) {
            return true;
        }
        return false;
    }


}
