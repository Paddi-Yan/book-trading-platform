package com.turing.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月20日 13:24:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("微信用户信息")
public class WechatUserInfo
{
    private String openId;
    private String nickname;
    private String gender;
    private String city;
    private String province;
    private String country;
    /**
     * 头像
     */
    private String avatarUrl;
    private String unionId;
}
