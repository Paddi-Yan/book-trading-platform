package com.turing.entity.dto;

import com.turing.entity.User;
import com.turing.entity.WechatUserInfo;
import com.turing.interceptor.UserThreadLocal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月20日 13:50:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable
{
    private Long id;
    private String nickname;
    private String username;
    private String password;
    private String gender;
    private String mobile;
    private String openId;
    private String photo;

    //拓展信息

    private String token;

    public void transform(WechatUserInfo wechatUserInfo)
    {
        this.nickname = wechatUserInfo.getNickname();
        this.photo = wechatUserInfo.getAvatarUrl();
        this.username = "";
        this.password = "";
        this.mobile = "";
        this.gender = wechatUserInfo.getGender();
        this.openId = wechatUserInfo.getOpenId();
    }

    public void transform(User user)
    {
        BeanUtils.copyProperties(user,this);
        this.username = "";
        this.password = "";
        this.mobile = "";
    }


}
