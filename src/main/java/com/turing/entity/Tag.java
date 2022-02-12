package com.turing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月21日 15:46:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Tag", description = "分类标签信息")
public class Tag implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(required = true)
    private Integer id;
    @ApiModelProperty(required = true)
    private String name;

    @TableField("user_id")
    @ApiModelProperty(required = true)
    private Integer userId;

    public Tag (String name, Integer userId) {
        this.name = name;
        this.userId = userId;
    }
}
