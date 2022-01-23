package com.turing.service.impl;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.turing.common.RedisKey;
import com.turing.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月20日 12:41:24
 */
@Service
public class WechatServiceImpl implements WechatService
{
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String wechatDecrypt(String encryptedData, String sessionId, String vi) throws Exception
    {
        //解密
        String json = (String) redisTemplate.opsForValue().get(RedisKey.WX_SESSION_ID + sessionId);
        JSONObject jsonObject = JSON.parseObject(json);
        String sessionKey = (String) jsonObject.get("session_key");
        byte[] encData = Base64.decode(encryptedData);
        byte[] iv = Base64.decode(vi);
        byte[] key = Base64.decode(sessionKey);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);
        return new String(cipher.doFinal(encData),"UTF-8");
    }
}
