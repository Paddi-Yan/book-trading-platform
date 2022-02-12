package com.turing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Q&A
 *
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月29日 13:04:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("qa")
@ApiModel(value = "QuestionAndAnswer", description = "活动Q&A信息")
public class QuestionAndAnswer {
    @ApiModelProperty(hidden = true)
    @TableId(type = IdType.AUTO)
    private Long id;
    private String question;
    private String answer;
    @ApiModelProperty(hidden = true)
    private Long activityId;
}
