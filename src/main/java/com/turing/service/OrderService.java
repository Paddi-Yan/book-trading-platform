package com.turing.service;

import com.turing.common.Result;
import com.turing.entity.dto.OrderDto;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年02月10日 10:54:42
 */
public interface OrderService
{

    Result submit(OrderDto orderDto);

    Result getOrderInfo(Long userId);

    Result payForOrder(Long userId, Long orderId);

    Result getOrderDetail(Long userId,Long orderId);

    Result createOrderFromCart(Long userId, Long cartId, Integer addressId);

    Result confirmOrder(Long orderId, Long userId);
}
