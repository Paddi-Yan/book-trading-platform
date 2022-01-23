package com.turing.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月20日 13:18:33
 */
@Data
public class WXAuthInfo
{
    @ApiModelProperty(required = true)
    private String encryptedData;
    @ApiModelProperty(required = true)
    private String iv;
    @ApiModelProperty(required = true)
    private String sessionId;
}
