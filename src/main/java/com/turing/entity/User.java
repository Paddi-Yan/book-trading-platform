package com.turing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.turing.entity.dto.UserDto;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月19日 23:54:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户信息")
public class User
{
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @TableField(value = "open_id")
    private String openId;
    private String nickname;
    private String gender;
    private String photo;

    /**
     * 以下信息属于用户私密信息
     * 转为UserDto
     * 将这些私密信息置为空
     */
    private String username;
    private String password;
    private String mobile;


    public void transform(UserDto userDto)
    {
        BeanUtils.copyProperties(userDto,this);
    }
}
