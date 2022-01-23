package com.turing.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月20日 14:14:56
 */
@Slf4j
public class JWTUtils
{
    public static final String AUTH_HEADER_KEY = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";
    /**
     * Token 过期时间为一周
     */
    public static final Long EXPIRE_TIME = 7 * 24 * 60 * 60 *1000L;
//    public static final Long EXPIRE_TIME = 1000L;

    private static final String SECRET = "Turing";

    private static final Logger logger = LoggerFactory.getLogger(Logger.class);

    /**
     * 校验token
     * @param token
     * @return
     */
    public static boolean verify(String token)
    {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT jwt = jwtVerifier.verify(token);
            log.info("Token令牌'{}'校验成功! ",token);
            return true;
        }catch (TokenExpiredException e) {
            log.error("Token令牌'{}'已过期，请重新登录! ",token);
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (JWTVerificationException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Long getUserId(String token)
    {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("id").asLong();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String sign(Long id)
    {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.create()
                    .withClaim("id",id)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (JWTCreationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Boolean isExpired(String token)
    {
        DecodedJWT jwt = JWT.decode(token);
        Date date = jwt.getExpiresAt();
        return date.before(new Date());
    }

}
