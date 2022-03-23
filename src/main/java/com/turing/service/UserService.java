package com.turing.service;

import com.turing.common.Result;
import com.turing.entity.User;
import com.turing.entity.dto.BookDto;
import com.turing.entity.dto.UserDto;
import com.turing.entity.dto.WechatLoginInfo;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月20日 01:23:55
 */
public interface UserService
{


    Result getUserInfo( Boolean refreshToken);

    User getUserById(Integer userId);

    Result getBookInfo(Integer userId, Integer type);

    Result updateBookInfo(BookDto bookDto);

    Result withdrawBookInfo(Integer bookId, Integer userId);

    Result deleteHistory(Integer bookId, Integer userId);

    User getUserByOpenId(String openid);
}
