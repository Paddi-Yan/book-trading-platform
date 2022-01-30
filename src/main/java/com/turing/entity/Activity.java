package com.turing.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.turing.entity.dto.ActivityDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月29日 12:21:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activity
{
    private Long id;
    /**
     * 活动名称
     */
    private String name;
    /**
     * 活动内容
     */
    private String content;
    /**
     * 活动创建时间
     */
    private Timestamp createdTime;
    /**
     * 活动状态
     * 0：审核中
     * 1：有效
     * -1：已失效
     */
    private byte status;
    /**
     * 活动封面图片
     */
    private String cover;
    /**
     * 活动开始时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date startTime;
    /**
     * 活动截止时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date deadline;
    /**
     * 活动发起人编号
     */
    private Long userId;
    /**
     * 活动标签
     */
    private String tags;

    public void transform(ActivityDto activityDto)
    {
        BeanUtils.copyProperties(activityDto,this);
        StringBuilder stringBuilder = new StringBuilder();
        List<String> tags = activityDto.getTags();
        for (int i = 0; i < tags.size(); i++) {
            if (i < tags.size() - 1)
            {
                stringBuilder.append(tags.get(i)+",");
            }else
            {
                stringBuilder.append(tags.get(i));
            }
        }
        this.tags = stringBuilder.toString();
    }

}
