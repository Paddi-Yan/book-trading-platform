package com.turing.service;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月20日 12:40:03
 */
public interface WechatService
{
    String wechatDecrypt(String encryptedData,String sessionId,String vi) throws Exception;
}
