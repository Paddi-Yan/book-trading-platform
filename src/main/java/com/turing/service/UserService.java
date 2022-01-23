package com.turing.service;

import com.turing.common.Result;
import com.turing.entity.User;
import com.turing.entity.WXAuthInfo;
import com.turing.entity.dto.BookDto;
import com.turing.entity.dto.UserDto;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月20日 01:23:55
 */
public interface UserService
{
    String getSessionId(String code);

    Result authLogin(WXAuthInfo wxAuthInfo);

    Result registry(UserDto userDto);

    Result login(UserDto userDto);

    Result getUserInfo( Boolean refreshToken);

    User getUserById(Integer userId);

    Result getBookInfo(Integer userId, Integer type);

    Result updateBookInfo(BookDto bookDto);

    Result withdrawBookInfo(Integer bookId, Integer userId);

    Result deleteHistory(Integer bookId, Integer userId);
}
