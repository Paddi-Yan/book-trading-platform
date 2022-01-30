package com.turing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.sql.Blob;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author qds
 * @since 2022-01-26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("community_infor")
@ApiModel(value = "CommunityInfor", description = "社区信息")
public class CommunityInfor implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("社区id")
    @TableId(value = "com_id", type = IdType.AUTO)
    private Long comId;

    @ApiModelProperty("社区名称")
    private String comName;

    @ApiModelProperty("社区头像")
    private String comPhoto;

    @ApiModelProperty("社区信息介绍")
    private String comInfor;

    @Override
    public String toString() {
        return "CommunityInfor{" +
        "comId=" + comId +
        ", comName=" + comName +
        ", comPhoto=" + comPhoto +
        ", comInfor=" + comInfor +
        "}";
    }
}
