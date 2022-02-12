package com.turing.interceptor;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.turing.common.HttpStatusCode;
import com.turing.common.RedisKey;
import com.turing.common.Result;
import com.turing.entity.dto.UserDto;
import com.turing.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月21日 00:12:01
 */
@Component
@Slf4j
public class TokenVerifyInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle (HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断请求url是否是请求controller方法
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //有些接口不需要做登录拦截 开发自定义注解@NoNeedToAuthorized-标注注解表示不需要登录就可以访问
        if (handlerMethod.hasMethodAnnotation(NoNeedToAuthorized.class)) {
            return true;
        }

        //获取token
        String token = request.getHeader(JWTUtils.AUTH_HEADER_KEY);
        if (StringUtils.isBlank(token)) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSON.toJSONString(new Result().fail(HttpStatusCode.UNAUTHORIZED)));
            return false;
        }
        token = token.replace(JWTUtils.TOKEN_PREFIX, "");
        //验证token
        boolean verify = JWTUtils.verify(token);
        //token认证通过放行/不通过返回未登录状态
        if (!verify) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSON.toJSONString(new Result().fail(HttpStatusCode.UNAUTHORIZED)));
            return false;
        }
        //token验证通过-从redis中获取用户信息
        UserDto userDto = (UserDto) redisTemplate.opsForValue().get(RedisKey.TOKEN + token);
        if (userDto == null) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSON.toJSONString(new Result().fail(HttpStatusCode.UNAUTHORIZED)));
            return false;
        }

        //得到UserInfo-放入ThreadLocal-方便之后的程序获取
        UserThreadLocal.putUserInfoToThread(userDto);
        return true;
    }

}
