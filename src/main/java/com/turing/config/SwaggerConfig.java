package com.turing.config;

import com.turing.common.HttpStatusCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月19日 23:35:51
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig
{
    @Value("${spring.swagger2.enabled}")
    private Boolean enabled;

    @Bean
    public Docket createRestApi()
    {
        List<ResponseMessage> messageList = new ArrayList<>();
        Arrays.stream(HttpStatusCode.values()).forEach(statusCode -> {
            messageList.add(
                    new ResponseMessageBuilder().code(statusCode.getCode()).message(statusCode.getMessage()).responseModel(
                            new ModelRef(statusCode.getMessage())).build()
            );
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
                .securitySchemes(security());
    }

    public ApiInfo apiInfo()
    {
        return new ApiInfoBuilder()
                .title("微信小程序接口文档"+"\t"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                .description("Wechat Mini Programmer API Document")
                .version("1.0")
                .build();
    }

    private List<ApiKey> security() {
        return new ArrayList<ApiKey>(
                Collections.singleton(new ApiKey("Token", "Bear Token", "header"))
        );
    }
}
