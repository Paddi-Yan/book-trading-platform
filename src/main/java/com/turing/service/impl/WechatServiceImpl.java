package com.turing.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import java.io.IOException;
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
        log.info("appid:{}",appid);
        log.info("secret:{}",secret);
        //调用微信登录凭证校验接口
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
        String replaceUrl = url.replace("APPID",appid).replace("SECRET", secret).replace("JSCODE", code).replace("'","");
        log.info("URL:{}",replaceUrl);
        //发起http请求获取微信的返回结果
        return HttpUtil.get(replaceUrl);
    }

    @Override
    public Result wechatLogin(String openid, String sessionKey, WechatUserInfo wechatUserInfo)
    {
        //通过openid唯一标识取查询数据库是否有用户信息
        User user = userService.getUserByOpenId(openid);
        System.out.println(user.toString());
        System.out.println(wechatUserInfo.toString());
        //数据查询为空
        if (user == null)
        {
            //用户注册
            user = new User();
            user.transform(wechatUserInfo);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            user.setRegisterTime(Timestamp.valueOf(simpleDateFormat.format(new Date())));
            return this.registry(user);
        }else
        {
            //用户登录
            user.setLastLoginIP(wechatUserInfo.getLastLoginIP());
            return this.login(user);
        }
    }

    @Override
    public Result getUserInfo(WechatUserInfo wechatUserInfo)
    {
        if (wechatUserInfo == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("用户信息不能为空!");
        }
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("openid", wechatUserInfo.getOpenid()));
        user.setNickname(wechatUserInfo.getNickname());
        user.setAvatar(wechatUserInfo.getAvatar());
        if (wechatUserInfo.getSessionKey()!=null && wechatUserInfo.getEncryptedData()!=null && wechatUserInfo.getIv()!=null)
        {
            String decryptJson = DecryptUtils.decrypt(wechatUserInfo.getSessionKey(), wechatUserInfo.getEncryptedData(), wechatUserInfo.getIv());
            log.info("解密后的数据:{}",decryptJson);
            JSONObject jsonObject = JSON.parseObject(decryptJson);
            user.setGender(String.valueOf(jsonObject.getInteger("gender")));
            user.setCity(jsonObject.getString("city"));
            user.setProvince(jsonObject.getString("province"));
            user.setCountry(jsonObject.getString("country"));
        }
        userMapper.updateById(user);
        UserDoc userDoc = new UserDoc();
        userDoc.transform(user);
        try {
            operateElasticsearchData(userDoc);
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("Elasticsearch插入用户数据失败===>>>{}",userDoc);
        }
        UserDto userDto = new UserDto();
        userDto.transform(user);
        return new Result().success(userDto);
    }

    public Result login(User user)
    {
        //签发Token
        String token = JWTUtils.sign(user.getId());
        UserDto userDto = new UserDto();
        userDto.transform(user);
        userDto.setToken(token);
        log.info("用户[{}]登录凭证[{}]",userDto,RedisKey.TOKEN+token);
        //存入缓存
        redisTemplate.opsForValue().set(RedisKey.TOKEN+token,userDto,7, TimeUnit.DAYS);
        return new Result().success(userDto);
    }

    public Result registry(User user)
    {
        UserDoc userDoc = null;
        userMapper.insert(user);
        try {
            userDoc = new UserDoc();
            userDoc.transform(user);
            operateElasticsearchData(userDoc);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Elasticsearch插入用户数据失败===>>>{}",userDoc);
            return new Result().fail(HttpStatusCode.ERROR);
        }
        return this.login(user);
    }

    private void operateElasticsearchData(UserDoc userDoc) throws IOException
    {
        IndexRequest request = new IndexRequest(ElasticsearchIndex.USER).id(userDoc.getId().toString());
        String jsonString = JSON.toJSONString(userDoc);
        request.source(jsonString, XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
        log.info("Elasticsearch插入用户数据成功===>>>{}", userDoc);
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
