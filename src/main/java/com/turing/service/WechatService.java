package com.turing.service;

import com.turing.common.Result;
import com.turing.entity.User;
import com.turing.entity.dto.WechatLoginInfo;
import com.turing.entity.dto.WechatUserInfo;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月20日 12:40:03
 */
public interface WechatService
{
    String getSessionKey(String code);

    Result wechatLogin(String openid, String sessionKey, WechatUserInfo wechatUserInfo);


    Result getUserInfo(WechatUserInfo wechatUserInfo);
}
