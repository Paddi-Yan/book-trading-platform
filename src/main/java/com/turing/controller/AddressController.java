package com.turing.controller;

import com.turing.common.HttpStatusCode;
import com.turing.common.Result;
import com.turing.entity.Address;
import com.turing.entity.User;
import com.turing.service.AddressService;
import com.turing.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月22日 23:37:04
 */
@RestController
@RequestMapping("/address")
@Api(description = "常用收货地址信息", tags = "AddressController")
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private UserService userService;

    private final Pattern pattern = Pattern.compile("^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\\d{8}$");

    @PostMapping("/addAddress")
    @ResponseBody
    @ApiOperation("添加常用收货地址")
    public Result addAddress (@RequestBody Address address) {
        User user = userService.getUserById(address.getUserId());
        if (user == null) {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("用户编号不存在,无法添加地址!");
        }
        Matcher matcher = pattern.matcher(address.getMobile());
        if (!matcher.find()) {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("联系方式不合法,无法添加地址!");
        }
        return addressService.addAddress(address);
    }

    @GetMapping("/getAddress")
    @ResponseBody
    @ApiOperation("获取常用收货地址")
    public Result getAddress (@RequestParam("userId") Integer userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("用户编号不存在,无法获取该用户的常用地址!");
        }
        return addressService.getAddress(userId);
    }

    @PutMapping("/editAddress/{id}")
    @ResponseBody
    @ApiOperation("修改常用收货地址")
    public Result editAddress (@RequestBody Address address, @PathVariable(name = "id", value = "id", required = true) Integer id) {
        address.setId(id);
        User user = userService.getUserById(address.getUserId());
        if (user == null) {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("用户编号不存在,无法修改地址!");
        }
        Matcher matcher = pattern.matcher(address.getMobile());
        if (!matcher.find()) {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("联系方式不合法,无法修改地址!");
        }
        return addressService.editAddress(address);
    }

    @DeleteMapping("/deleteAddress")
    @ResponseBody
    @ApiOperation("单个/批量删除常用收获地址")
    public Result deleteAddress (@RequestParam Integer userId, @RequestBody List<Integer> addressIdList) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("用户编号不存在,无法修改地址!");
        }
        return addressService.deleteAddress(userId, addressIdList);
    }
}
