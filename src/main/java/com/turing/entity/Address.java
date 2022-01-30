package com.turing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月22日 23:27:18
 */
@Data
@AllArgsConstructor
@ApiModel(value = "Address",description = "常用地址信息")
public class Address
{
    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty(hidden = true,name = "id")
    private Integer id;

    @TableField(value = "user_id")
    @ApiModelProperty(value = "所属用户ID",required = true)
    private Integer userId;

    @ApiModelProperty(value = "收货人",required = true)
    private String username;

    @ApiModelProperty(value = "联系电话",required = true)
    private String mobile;

    @ApiModelProperty(value = "地区",required = true)
    private String area;

    @TableField(value = "detail_address")
    @ApiModelProperty(value = "详细地址",required = true)
    private String detailAddress;
}
