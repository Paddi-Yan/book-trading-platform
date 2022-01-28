package com.turing.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.turing.common.ElasticsearchIndex;
import com.turing.common.HttpStatusCode;
import com.turing.common.RedisKey;
import com.turing.common.Result;
import com.turing.entity.User;
import com.turing.entity.dto.UserDto;
import com.turing.entity.dto.WechatLoginInfo;
import com.turing.entity.dto.WechatUserInfo;
import com.turing.entity.elasticsearch.UserDoc;
import com.turing.mapper.UserMapper;
import com.turing.service.UserService;
import com.turing.service.WechatService;
import com.turing.utils.DecryptUtils;
import com.turing.utils.IPUtils;
import com.turing.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月20日 12:41:24
 */
@Service
@Slf4j
public class WechatServiceImpl implements WechatService
{
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Value("${wechat-mini.appid}")
    private String appid;

    @Value("${wechat-mini.secret}")
    private String secret;

    @Autowired
    private RestHighLevelClient client;

    @Override
    public String getSessionKey(String code)
    {
        //调用微信登录凭证校验接口
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
        String replaceUrl = url.replace("APPID", appid).replace("SECRET", secret).replace("JSCODE", code);
        //发起http请求获取微信的返回结果
        return HttpUtil.get(replaceUrl);
    }

    @Override
    public Result wechatLogin(String openid, String sessionKey, WechatLoginInfo wechatLoginInfo)
    {
        User user = userService.getUserByOpenId(openid);
        WechatUserInfo wechatUserInfo = wechatLoginInfo.getWechatUserInfo();
        String decryptJson = DecryptUtils.decrypt(sessionKey, wechatUserInfo.getEncryptedData(), wechatUserInfo.getIv());
        JSONObject jsonObject = JSON.parseObject(decryptJson);
        log.info("解密后的数据:{}",jsonObject);
        String nickname = jsonObject.getString("nickName");
        if (wechatUserInfo.getNickname() == null && !StringUtils.isBlank(nickname))
        {
            wechatUserInfo.setNickname(nickname);
        }
        wechatUserInfo.setGender(String.valueOf(jsonObject.getInteger("gender")));
        wechatUserInfo.setCity(jsonObject.getString("city"));
        wechatUserInfo.setProvince(jsonObject.getString("province"));
        wechatUserInfo.setCountry(jsonObject.getString("country"));

        //对数据进行解密
        if (user == null)
        {
            //用户注册
            user.transform(wechatUserInfo);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            user.setRegisterTime(Timestamp.valueOf(simpleDateFormat.format(new Date())));
            return this.registry(user);
        }else
        {
            //用户登录
            return this.login(user);
        }

    }

    private Result login(User user)
    {
        //签发Token
        String token = JWTUtils.sign(user.getId());
        UserDto userDto = new UserDto();
        userDto.transform(user);
        userDto.setToken(token);
        //存入缓存
        redisTemplate.opsForValue().set(RedisKey.TOKEN+token,userDto,7, TimeUnit.DAYS);

        return new Result().success(userDto);
    }

    @Override
    public Result registry(User user)
    {
        UserDoc userDoc = null;
        try {
            userMapper.insert(user);
            IndexRequest request = new IndexRequest(ElasticsearchIndex.USER).id(user.getId().toString());
            userDoc = new UserDoc();
            userDoc.transform(user);
            String jsonString = JSON.toJSONString(userDoc);
            request.source(jsonString, XContentType.JSON);
            client.index(request, RequestOptions.DEFAULT);
            log.info("Elasticsearch插入用户数据成功===>>>{}",userDoc);
            return this.login(user);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Elasticsearch插入用户数据失败===>>>{}",userDoc);
            return new Result().fail(HttpStatusCode.ERROR);
        }
    }



/*
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
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);
        return new String(cipher.doFinal(encData),"UTF-8");
    }
*/
}
