package com.turing.interceptor;

import java.lang.annotation.*;

/**
 * 该注解用于表示该方法不需要进行token验证
 *
 * @author 又蠢又笨的懒羊羊快递员
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoNeedToAuthorized {

}
