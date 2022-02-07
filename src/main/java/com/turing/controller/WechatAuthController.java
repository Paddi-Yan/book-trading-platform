package com.turing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.turing.common.HttpStatusCode;
import com.turing.common.RedisKey;
import com.turing.common.Result;
import com.turing.entity.dto.UserDto;
import com.turing.entity.dto.WechatLoginInfo;
import com.turing.entity.dto.WechatUserInfo;
import com.turing.interceptor.NoNeedToAuthorized;
import com.turing.service.UserService;
import com.turing.service.WechatService;
import com.turing.utils.IPUtils;
import com.turing.utils.JWTUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月23日 22:59:31
 */
@RequestMapping("/auth")
@Api(description = "微信用户登录",tags = "WechatAuthController")
@RestController
@Slf4j
public class WechatAuthController
{
    @Autowired
    private WechatService wechatService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/login")
    @ApiOperation("微信一键登录")
    @ResponseBody
    @NoNeedToAuthorized
    public Result login(@RequestParam String code,
                        @RequestParam String nickname ,
                        @RequestParam String avatar ,
                        @RequestParam String gender,
                        HttpServletRequest request)
    {
        if (code == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("请求携带参数不完整,登录失败!");
        }
        //获取用户sessionKey和openid
        String result = wechatService.getSessionKey(code);
        JSONObject jsonObject = JSON.parseObject(result);
        log.info("调用微信登录凭证校验接口返回结果:{}",jsonObject.toJSONString());
        String openid = jsonObject.getString("openid");
        log.info("用户唯一标识:{}",openid);
        String sessionKey = jsonObject.getString("session_key");
        log.info("登录会话密钥:{}",sessionKey);
        if (openid == null || sessionKey == null)
        {
            return new Result().fail(HttpStatusCode.ERROR).message("调用官方接口错误!");
        }
        //登录逻辑
        WechatUserInfo wechatUserInfo = new WechatUserInfo();
        wechatUserInfo.setLastLoginIP(IPUtils.getIpAddr(request));
        wechatUserInfo.setAvatar(avatar);
        wechatUserInfo.setNickname(nickname);
        wechatUserInfo.setOpenid(openid);
        wechatUserInfo.setGender(gender);
        Result loginResult = wechatService.wechatLogin(openid, sessionKey, wechatUserInfo);
        redisTemplate.opsForValue().set(RedisKey.SESSION_KEY+sessionKey,openid,7, TimeUnit.DAYS);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("openid",openid);
        resultMap.put("sessionKey",sessionKey);
        resultMap.put("userInfo",loginResult.getData());
        return new Result().success(resultMap);
    }

    @GetMapping("/getWechatUserInfo")
    @ApiOperation("获取微信用户信息")
    @ResponseBody
    public Result getUserInfo(@RequestBody WechatUserInfo wechatUserInfo)
    {
        return wechatService.getUserInfo(wechatUserInfo);
    }


    @PostMapping("/logout/{userId}")
    @ApiOperation("用户注销登录")
    @ResponseBody
    public Result logout(@PathVariable Long userId,HttpServletRequest request)
    {
        log.info("[请求开始]注销登录,请求参数，userId:{}", userId);
        if (userId == null)
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR);
        }
        String token = request.getHeader(JWTUtils.AUTH_HEADER_KEY);
        token = token.replace(JWTUtils.TOKEN_PREFIX,"");
        UserDto userDto = (UserDto) redisTemplate.opsForValue().get(RedisKey.TOKEN + token);
        if (!userId.equals(userDto.getId()))
        {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR);
        }
        redisTemplate.delete(RedisKey.TOKEN + token);
        log.info("[请求结束]用户{}注销登录成功",userId);
        return new Result().success("用户注销成功!");
    }

}
