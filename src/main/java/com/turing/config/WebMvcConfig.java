package com.turing.config;

import com.turing.interceptor.CrossInterceptor;
import com.turing.interceptor.OptionsInterceptor;
import com.turing.interceptor.TokenVerifyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月21日 00:13:19
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private TokenVerifyInterceptor tokenVerifyInterceptor;

    @Resource
    private CrossInterceptor crossInterceptor;

    @Resource
    private OptionsInterceptor optionsInterceptor;

    @Override
    public void addInterceptors (InterceptorRegistry registry) {
        registry.addInterceptor(crossInterceptor);
        registry.addInterceptor(optionsInterceptor);
        registry.addInterceptor(tokenVerifyInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger-ui.html/**", "/swagger-resources/**", "/webjars/**", "/v2/**");
    }
}
