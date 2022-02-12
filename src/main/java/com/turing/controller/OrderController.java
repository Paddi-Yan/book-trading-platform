package com.turing.controller;

import com.turing.common.Result;
import com.turing.entity.dto.OrderDto;
import com.turing.interceptor.NoNeedToAuthorized;
import com.turing.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年02月10日 10:53:42
 */
@RestController
@RequestMapping("/order")
@Api(description = "处理订单接口", tags = "OrderController")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @ResponseBody
    @PostMapping("/submit")
    @ApiOperation("商品页立即购买")
    @NoNeedToAuthorized
    public Result createOrder (@RequestBody OrderDto orderDto) {
        return orderService.submit(orderDto);
    }

    @ResponseBody
    @GetMapping("/getOrder/{userId}")
    @ApiOperation("获取用户的订单列表")
    @NoNeedToAuthorized
    public Result getOrderInfo (@PathVariable Long userId) {
        return orderService.getOrderInfo(userId);
    }

    @ResponseBody
    @PostMapping("/payForOrder/{userId}/{orderId}")
    @ApiOperation("支付订单")
    @NoNeedToAuthorized
    public Result payForOrder (@PathVariable Long orderId, @PathVariable Long userId) {
        return orderService.payForOrder(userId, orderId);
    }

    @ResponseBody
    @GetMapping("/getOrderDetail/{userId}/{orderId}")
    @ApiOperation("获取用户的订单详情")
    @NoNeedToAuthorized
    public Result getOrderDetail (@PathVariable Long orderId, @PathVariable Long userId) {
        return orderService.getOrderDetail(userId, orderId);
    }

    @ResponseBody
    @PostMapping("/submitFromCart/{userId}")
    @ApiOperation("购物车购买")
    @NoNeedToAuthorized
    public Result createOrderFromCart (@PathVariable Long userId, @RequestParam Long cartId, @RequestParam Integer addressId) {
        return orderService.createOrderFromCart(userId, cartId, addressId);
    }

    @ResponseBody
    @PostMapping("/confirm/{userId}/{orderId}")
    @ApiOperation("确认收货")
    @NoNeedToAuthorized
    public Result confirmOrder (@PathVariable Long orderId, @PathVariable Long userId) {
        return orderService.confirmOrder(orderId, userId);
    }


}
