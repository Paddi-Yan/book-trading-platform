package com.turing.config;

import com.turing.common.HttpStatusCode;
import com.turing.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月19日 23:35:51
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${spring.swagger2.enabled}")
    private Boolean enabled;

    @Bean
    public Docket createRestApi () {
        List<ResponseMessage> messageList = new ArrayList<>();
        Arrays.stream(HttpStatusCode.values()).forEach(statusCode -> {
            messageList.add(new ResponseMessageBuilder().code(statusCode.getCode())
                    .message(statusCode.getMessage())
                    .responseModel(new ModelRef(statusCode.getMessage()))
                    .build());
        });

        return new Docket(DocumentationType.SWAGGER_2)
                //全局响应状态码
                .globalResponseMessage(RequestMethod.GET, messageList)
                .globalResponseMessage(RequestMethod.POST, messageList)
                .globalResponseMessage(RequestMethod.PUT, messageList)
                .globalResponseMessage(RequestMethod.DELETE, messageList)
                .apiInfo(apiInfo())
                .enable(enabled)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.turing.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(security())
                .securityContexts(securityContexts());
    }

    public ApiInfo apiInfo () {
        return new ApiInfoBuilder().title("微信小程序接口文档" + "\t" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                .description("Wechat Mini Programmer API Document")
                .version("1.0")
                .build();
    }

    private List<ApiKey> security () {
        List<ApiKey> apiKeyList = new ArrayList<>();
        String authHeaderKey = JWTUtils.AUTH_HEADER_KEY;
        apiKeyList.add(new ApiKey(authHeaderKey, authHeaderKey, "header"));
        return apiKeyList;
    }

    private List<SecurityContext> securityContexts () {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("^(?!auth).*$"))
                .build());
        return securityContexts;
    }

    private List<SecurityReference> defaultAuth () {
        AuthorizationScope authorizationScope = new AuthorizationScope("Global Token Authorization", "Bearer {token} can access everything.");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference(JWTUtils.AUTH_HEADER_KEY, authorizationScopes));
        return securityReferences;
    }
}
