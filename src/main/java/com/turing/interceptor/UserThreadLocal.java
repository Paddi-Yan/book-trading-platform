package com.turing.interceptor;

import com.turing.entity.dto.UserDto;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月21日 00:32:30
 */
public class UserThreadLocal {
    private static final ThreadLocal<UserDto> threadLocal = new ThreadLocal<>();

    public static void putUserInfoToThread (UserDto userDto) {
        threadLocal.set(userDto);
    }

    public static UserDto getUserInfoFromThread () {
        return threadLocal.get();
    }

    public static void remove () {
        threadLocal.remove();
    }


}
