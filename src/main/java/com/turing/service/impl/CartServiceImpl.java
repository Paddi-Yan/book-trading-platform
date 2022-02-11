package com.turing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.turing.common.*;
import com.turing.entity.Book;
import com.turing.entity.Cart;
import com.turing.entity.User;
import com.turing.mapper.BookMapper;
import com.turing.mapper.CartMapper;
import com.turing.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月30日 15:52:58
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class CartServiceImpl implements CartService
{

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Result addCart(User user, Integer bookId, Integer count)
    {
        Book book = bookMapper.selectOne(new QueryWrapper<Book>().eq("id", bookId).eq("status", "1").gt("stock",0));
        if (book == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("该书籍不存在或者已失效,加入购物车失败!");
        }
        Cart cart = new Cart();
        cart.transform(user,book);
        cart.setCount(count);
        cart.setTotal(book.getSellingPrice().multiply(BigDecimal.valueOf(count)));
        int insert = cartMapper.insert(cart);
        if (insert == 1)
        {
            redisTemplate.opsForHash().put(RedisKey.CART_KEY+ user.getId(),cart.getId().toString(),cart);
        }
        return new Result().success(cart);
    }

    @Override
    public Result getCart(Long userId, Integer refresh)
    {
        //不需要刷新缓存
        if (refresh == null || refresh.intValue() != 1)
        {
            return new Result().success(redisTemplate.opsForHash().entries(RedisKey.CART_KEY + userId));
        }
        //需要刷新缓存
        else if (refresh.intValue() == 1)
        {
            List<Cart> cartList = cartMapper.selectList(new QueryWrapper<Cart>().eq("user_id", userId));
            Map<String,Cart> cartMap = new HashMap<>();
            //遍历查询出来的用户购物车物品
            for (Cart cart : cartList) {
                Book book = bookMapper.selectById(cart.getBookId());
                //书籍已经失效
                if (BookStatus.INVALID.getCode().equals(book.getStatus()))
                {
                    //修改购物车物品状态
                    cart.setDeleted(CartStatus.DELETED.getCode());
                    cartMapper.updateById(cart);
                }
                cartMap.put(cart.getId().toString(),cart);
            }
            redisTemplate.opsForHash().putAll(RedisKey.CART_KEY+userId,cartMap);
            return new Result().success(cartList);
        }
        return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR);
    }

    @Override
    public Result deleteCart(Long userId, List<Long> cartIdList)
    {
        List<Cart> carts = cartMapper.selectBatchIds(cartIdList);
        //校验这些购物车物品是否都属于该用户
        if (carts == null || carts.size() == 0 || carts.size() != cartIdList.size())
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("购物车物品不存在,删除失败!");
        }
        for (Cart cart : carts) {
            if (!userId.equals(cart.getUserId())) {
                return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("用户无删除该购物车物品权限!");
            }
        }
        int count = cartMapper.deleteBatchIds(cartIdList);

        if (count == cartIdList.size())
        {
            log.info("删除购物车物品成功===>>>{}",carts);
            redisTemplate.execute(new SessionCallback()
            {
                @Override
                public Object execute(RedisOperations operations) throws DataAccessException
                {
                    operations.multi();
                    for (Long id : cartIdList) {
                        operations.opsForHash().delete(RedisKey.CART_KEY+userId,id.toString());
                    }
                    try {
                        operations.exec();
                    } catch (Exception e) {
                        log.warn("Redis提交事务失败!===>>>{}",e.getMessage());
                    }
                    log.info("Redis成功删除购物车信息===>>>{}",carts);
                    return null;
                }
            });
        }

        return new Result().success(carts);
    }
}
