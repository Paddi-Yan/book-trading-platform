package com.turing.entity.dto;

import com.turing.entity.User;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月20日 13:50:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "UserDto",description = "用户信息")
public class UserDto implements Serializable
{
    private static final long serialVersionUID = -3992971186990897627L;
    private Long id;
    private String nickname;
    private String username;
    private String password;
    private String gender;
    private String mobile;
    private String openid;
    private String avatar;

    //拓展信息
    private String token;

    public void transform(WechatUserInfo wechatUserInfo)
    {
//        this.id = wechatUserInfo.getUserId();
        this.nickname = wechatUserInfo.getNickname();
        this.avatar = wechatUserInfo.getAvatar();
        this.username = "";
        this.password = "";
        this.mobile = "";
        this.gender = String.valueOf(wechatUserInfo.getGender());
        this.openid = wechatUserInfo.getOpenid();
    }

    public void transform(User user)
    {
        BeanUtils.copyProperties(user,this);
        this.username = "";
        this.password = "";
        this.mobile = "";
    }


}
