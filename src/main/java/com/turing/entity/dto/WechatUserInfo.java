package com.turing.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月20日 13:24:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("WechatUserInfo")
public class WechatUserInfo implements Serializable {
    private static final long serialVersionUID = 8243422323180274689L;


    @ApiModelProperty(required = true)
    private String openid;
    @ApiModelProperty(hidden = true)
    private String nickname;

    @ApiModelProperty(hidden = true)
    private String lastLoginIP;
    /**
     * 头像
     */
    @ApiModelProperty(hidden = true)
    private String avatar;

    /**
     * 包括敏感数据在内的完整用户信息的加密数据
     */
    private String encryptedData;

    @ApiModelProperty(required = true)
    private String sessionKey;

    /**
     * 加密算法的初始向量
     */
    private String iv;

    /**
     * 以下信息新版本不再能够通过getUserProfile获取
     * 要通过encryptedData和iv解密获得
     * gender(性别) 0-未知 1-男 2-女
     */
    @ApiModelProperty(required = true)
    private String gender;
    @ApiModelProperty(hidden = true)
    private String city;
    @ApiModelProperty(hidden = true)
    private String province;
    @ApiModelProperty(hidden = true)
    private String country;


}
