package com.turing.entity.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月23日 23:38:50
 */
@Data
@ApiModel(value = "WechatLoginInfo", description = "微信登录信息")
public class WechatLoginInfo implements Serializable {
    private static final long serialVersionUID = 4344018462289153841L;

    private String code;

    private WechatUserInfo wechatUserInfo;
}
