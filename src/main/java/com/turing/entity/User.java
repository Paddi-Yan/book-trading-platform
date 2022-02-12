package com.turing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.turing.entity.dto.UserDto;
import com.turing.entity.dto.WechatUserInfo;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月19日 23:54:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "User", description = "用户信息")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String openid;
    private String nickname;
    private String gender;
    /**
     * 头像
     */
    private String avatar;

    /**
     * 以下信息属于用户私密信息
     * 转为UserDto
     * 将这些私密信息置为空
     */
    private String username;
    private String password;
    private String mobile;

    private String city;
    private String province;
    private String country;

    /**
     * 是否被禁用
     */
    @TableField("enabled")
    private byte isEnabled;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Timestamp registerTime;

    @TableField("last_login_ip")
    private String lastLoginIP;

    public void transform (UserDto userDto) {
        BeanUtils.copyProperties(userDto, this);
    }

    public void transform (WechatUserInfo wechatUserInfo) {
        BeanUtils.copyProperties(wechatUserInfo, this);
        switch (wechatUserInfo.getGender()) {
            case "0":
                this.setGender("女");
                break;
            case "1":
                this.setGender("男");
                break;
        }
    }


}
