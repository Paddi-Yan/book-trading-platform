package com.turing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
 * @since 2022-02-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("community_infor")
@ApiModel(value = "CommunityInfor对象", description = "")
public class CommunityInfor implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(hidden = true)
    @TableId(value = "com_id", type = IdType.AUTO)
    private Long comId;

    @ApiModelProperty("社区名称")
    private String comName;

    @ApiModelProperty(hidden = true)
    private String comPhoto;

    @ApiModelProperty("社区信息介绍")
    private String comInfor;

    @ApiModelProperty("创建人id")
    private Long userId;


}
