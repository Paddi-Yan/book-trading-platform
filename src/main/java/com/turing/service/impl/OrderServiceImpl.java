package com.turing.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.turing.common.*;
import com.turing.entity.*;
import com.turing.entity.dto.OrderDto;
import com.turing.entity.elasticsearch.BookDoc;
import com.turing.mapper.*;
import com.turing.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年02月10日 10:54:50
 */
@Service
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService
{
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private RestHighLevelClient client;

    @Override
    public Result submit(OrderDto orderDto)
    {
        Integer bookId = orderDto.getBookId();
        Book book = bookMapper.selectOne(new QueryWrapper<Book>().eq("id",bookId).eq("status", BookStatus.EFFECTIVE.getCode()));
        if (book == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("书籍不存在或已经失效,创建订单失败!");
        }
        Long userId = orderDto.getUserId();
        User user = userMapper.selectById(userId);
        if (user == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("用户不存在,创建订单失败!");
        }
        Integer count = orderDto.getCount();
        if (count <=0 || count > book.getStock())
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("商品数量非法,创建订单失败!");
        }
        //单价
        BigDecimal price = book.getSellingPrice();
        //总价
        BigDecimal total = price.multiply(BigDecimal.valueOf(count));
        long id = IdWorker.getId();
        orderDto.setId(id);
        orderDto.setTitle(book.getTitle());
        orderDto.setPrice(price);
        orderDto.setCreatedTime(LocalDateTime.now());
        orderDto.setCover(book.getCover());
        orderDto.setTotal(total);
        orderDto.setFreight(book.getFreight());
        orderDto.setStatus(OrderStatus.TO_BE_PAID.getStatus());
        redisTemplate.execute(new SessionCallback()
        {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException
            {
                operations.multi();
                operations.opsForHash().put(RedisKey.ORDER_KEY+userId,RedisKey.ORDER_FIELD+id,orderDto);
                operations.opsForValue().set(RedisKey.ORDER_FIELD+id,orderDto,30, TimeUnit.MINUTES);
                return operations.exec();
            }
        });
        return new Result().success(orderDto);
    }

    @Override
    public Result getOrderInfo(Long userId)
    {
        final Map<String, OrderDto>[] orders = new Map[]{null};
        redisTemplate.execute(new SessionCallback()
        {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException
            {
                orders[0] = operations.opsForHash().entries(RedisKey.ORDER_KEY + userId);
                ArrayList<OrderDto> orderList = new ArrayList<>();
                operations.multi();
                //遍历该用户的所有订单信息
                for (Map.Entry<String, OrderDto> map : orders[0].entrySet()) {
                    String key = map.getKey();
                    OrderDto orderDto = (OrderDto) operations.opsForValue().get(key);
                    //Hash中的订单信息过期了
                    if (orderDto == null) {
                        orderDto = map.getValue();
                        //订单状态设置为失效
                        orderDto.setStatus(OrderStatus.CANCELED.getStatus());
                    }
                }
                operations.opsForHash().putAll(RedisKey.ORDER_KEY + userId, orders[0]);
                return operations.exec();
            }
        });
        if (orders[0].isEmpty() || orders[0] == null)
        {
            return new Result().success("暂时无订单信息!");
        }
        return new Result().success(orders[0]);
    }

    @Override
    public Result payForOrder(Long userId, Long orderId)
    {
        User user = userMapper.selectById(userId);
        if (user == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("用户不存在,支付订单失败!");
        }
        OrderDto orderDto = (OrderDto) redisTemplate.execute(new SessionCallback()
        {
            @Override
            public OrderDto execute(RedisOperations operations) throws DataAccessException
            {
                OrderDto orderDtoInHash = (OrderDto) operations.opsForHash()
                        .get(RedisKey.ORDER_KEY + userId, RedisKey.ORDER_FIELD + orderId);
                OrderDto orderDtoInValue = (OrderDto) operations.opsForValue().get(RedisKey.ORDER_FIELD + orderId);
                if (orderDtoInValue == null && orderDtoInHash != null)
                {
                    orderDtoInHash.setStatus(OrderStatus.CANCELED.getStatus());
                    operations.opsForHash().put(RedisKey.ORDER_KEY + userId, RedisKey.ORDER_FIELD + orderId,orderDtoInHash);
                    return orderDtoInHash;
                }
                return orderDtoInHash;
            }
        });
        if (orderDto == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("订单不存在,支付失败!");
        }
        if (orderDto.getStatus().equals(OrderStatus.CANCELED.getStatus()))
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("订单已被取消,支付失败!");
        }
        log.info("订单信息:[{}]",orderDto);

        //判断书籍是否有效
        Book book = bookMapper.selectById(orderDto.getBookId());
        if (book == null || !BookStatus.EFFECTIVE.getCode().equals(book.getStatus()))
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("购买书籍不存在或已经失效,支付订单失败!");
        }
        //库存容量是否足够
        if (book.getStock() < orderDto.getCount())
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("商品数量不足,支付订单失败!");
        }
        //开始支付
        //支付回调返回成功结果
        orderDto.setStatus(OrderStatus.TO_BE_SHIPPED.getStatus());
        orderDto.setPayTime(LocalDateTime.now());
        redisTemplate.opsForValue().set(RedisKey.ORDER_FIELD+orderId,orderDto);
        redisTemplate.opsForHash().put(RedisKey.ORDER_KEY+userId,RedisKey.ORDER_FIELD+orderId,orderDto);
        //扣减书籍数量
        book.setStock(book.getStock() - orderDto.getCount());
        if (book.getStock() <= 0)
        {
            book.setStatus(BookStatus.INVALID.getCode());
        }
        //更新书籍信息
        bookMapper.updateById(book);
        //订单存入数据库
        Order order = new Order();
        order.transform(orderDto);
        orderMapper.insert(order);
        //更新ES数据
        BookDoc bookDoc = new BookDoc();
        bookDoc.transform(book);

        IndexRequest indexRequest = new IndexRequest(ElasticsearchIndex.BOOK).id(book.getId().toString());
        String jsonString = JSON.toJSONString(bookDoc);
        indexRequest.source(jsonString, XContentType.JSON);
        try {
            client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result().success(orderDto);
    }

    @Override
    public Result getOrderDetail(Long userId,Long orderId)
    {
        OrderDto orderDto = (OrderDto) redisTemplate.opsForHash().get(RedisKey.ORDER_KEY+userId,RedisKey.ORDER_FIELD+orderId);
        if (orderDto == null)
        {
            Order order = orderMapper.selectById(orderId);
            if (order == null)
            {
                return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("订单不存在,查询失败");
            }
            orderDto.transform(order);
            redisTemplate.opsForHash().put(RedisKey.ORDER_KEY+userId,RedisKey.ORDER_FIELD+orderId,orderDto);
        }
        Address address = addressMapper.selectById(orderDto.getAddressId());
        Map<String,Object> result = new HashMap<>();
        result.put("address",address);
        result.put("order",orderDto);
        return new Result().success(result);
    }

    @Override
    public Result createOrderFromCart(Long userId, Long cartId, Integer addressId)
    {
        User user = userMapper.selectById(userId);
        Cart cart = (Cart) redisTemplate.opsForHash().get(RedisKey.CART_KEY + userId, cartId.toString());
        Address address = addressMapper.selectById(addressId);
        if (cart == null || user == null || address == null)
        {
            cart = cartMapper.selectOne(new QueryWrapper<Cart>().eq("id", cartId).eq("user_id", userId));
            if (cart == null)
            {
                return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("参数传递错误,创建订单失败!");
            }
            if (CartStatus.DELETED.getCode().equals(cart.getDeleted()))
            {
                return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("购物车已提交订单,请勿重复提交!");
            }
        }
        Book book = bookMapper.selectById(cart.getBookId());
        if (book == null || !BookStatus.EFFECTIVE.getCode().equals(book.getStatus()))
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("购物车物品已失效或不存在,创建订单失败!");
        }
        if (book.getStock() < cart.getCount())
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("购物车中商品库存已不足,创建订单失败!");
        }
        OrderDto orderDto = new OrderDto();
        long id = IdWorker.getId();
        orderDto.setId(id);
        orderDto.setCover(cart.getBookPhoto());
        orderDto.setTitle(cart.getBookName());
        orderDto.setTotal(cart.getTotal());
        orderDto.setPrice(cart.getPrice());
        orderDto.setCount(cart.getCount());
        orderDto.setBookId(cart.getBookId());
        orderDto.setUserId(userId);
        orderDto.setCartId(cartId);
        orderDto.setAddressId(addressId);
        orderDto.setCreatedTime(LocalDateTime.now());
        orderDto.setFreight(book.getFreight());
        orderDto.setStatus(OrderStatus.TO_BE_PAID.getStatus());
        //清除购物车
        cart.setDeleted(CartStatus.DELETED.getCode());
        cartMapper.updateById(cart);
        redisTemplate.execute(new SessionCallback()
        {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException
            {
                operations.multi();
                //清除购物车
                operations.opsForHash().delete(RedisKey.CART_KEY+userId,cartId.toString());
                operations.opsForHash().put(RedisKey.ORDER_KEY+userId,RedisKey.ORDER_FIELD+id,orderDto);
                operations.opsForValue().set(RedisKey.ORDER_FIELD+id,orderDto,30, TimeUnit.MINUTES);
                return operations.exec();
            }
        });
        return new Result().success(orderDto);
    }

    @Override
    public Result confirmOrder(Long orderId, Long userId)
    {
        OrderDto orderDto = (OrderDto) redisTemplate.opsForHash().get(RedisKey.ORDER_KEY + userId, RedisKey.ORDER_FIELD + orderId);
        if (orderDto == null)
        {
            Order order = orderMapper.selectOne(new QueryWrapper<Order>().eq("id", orderId).eq("user_id", userId));
            if (order == null)
            {
                return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("订单不存在,确认收货失败!");
            }
            orderDto = new OrderDto();
            orderDto.transform(order);
            redisTemplate.opsForHash().put(RedisKey.ORDER_KEY + userId, RedisKey.ORDER_FIELD + orderId,orderDto);
        }
        //校验订单是否处于已签收状态
        if (!OrderStatus.SHIPPED_END.getStatus().equals(orderDto.getStatus()))
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("当前订单状态无法确认签收!");
        }
        orderDto.setStatus(OrderStatus.RECEIVED.getStatus());
        OrderDto finalOrderDto = orderDto;
        redisTemplate.execute(new SessionCallback()
        {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException
            {
                operations.multi();
                operations.opsForHash().put(RedisKey.ORDER_KEY + userId, RedisKey.ORDER_FIELD + orderId, finalOrderDto);
                operations.opsForValue().set(RedisKey.ORDER_FIELD + orderId, finalOrderDto);
                return operations.exec();
            }
        });
        Order order = new Order();
        order.transform(orderDto);
        orderMapper.updateById(order);
        return new Result().success(orderDto);
    }
}
