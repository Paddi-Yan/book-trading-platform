package com.turing.service;

import com.turing.common.Result;
import com.turing.entity.User;

import java.util.List;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月30日 15:52:48
 */
public interface CartService {

    Result addCart (User user, Integer bookId, Integer count);

    Result getCart (Long userId, Integer refresh);

    Result deleteCart (Long userId, List<Long> cartIdList);
}
