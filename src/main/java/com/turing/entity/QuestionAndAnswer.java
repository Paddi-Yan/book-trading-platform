package com.turing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Q&A
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月29日 13:04:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("qa")
public class QuestionAndAnswer
{
    @ApiModelProperty(hidden = true)
    private Long id;
    private String question;
    private String answer;
    @ApiModelProperty(hidden = true)
    private Long activityId;
}
