package com.turing.entity.dto;

import com.turing.entity.CommunityInfor;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author qds
 * @since 2022-02-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityInforDto implements Serializable {

    @ApiModelProperty(hidden = true)
    private Long comId;

    @ApiModelProperty("社区名称")
    private String comName;

    @ApiModelProperty(hidden = true)
    private String comPhoto;

    @ApiModelProperty("社区信息介绍")
    private String comInfor;

    @ApiModelProperty("创建人id")
    private Long userId;

    @ApiModelProperty("类别")
    private int kind;

    @ApiModelProperty("关注数")
    private Integer attention;

    @ApiModelProperty("话题数")
    private Integer topic;

    @ApiModelProperty("热度")
    private Integer heat;

    @ApiModelProperty("发表数")
    private Integer sentCount;

    public void transform(CommunityInfor communityInfor, Integer attention, Integer topic) {
        BeanUtils.copyProperties(communityInfor, this);
        this.attention = attention;
        this.topic = topic;
    }

    public void transform(CommunityInfor communityInfor, Integer attention, Integer topic, Integer heat, Integer sentCount) {
        BeanUtils.copyProperties(communityInfor, this);
        this.attention = attention;
        this.topic = topic;
        this.heat = heat;
        this.sentCount = sentCount;
    }

}
