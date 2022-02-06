package com.turing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author qds
 * @since 2022-02-06
 */
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


    public Long getComId() {
        return comId;
    }

    public void setComId(Long comId) {
        this.comId = comId;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getComPhoto() {
        return comPhoto;
    }

    public void setComPhoto(String comPhoto) {
        this.comPhoto = comPhoto;
    }

    public String getComInfor() {
        return comInfor;
    }

    public void setComInfor(String comInfor) {
        this.comInfor = comInfor;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "CommunityInfor{" +
        "comId=" + comId +
        ", comName=" + comName +
        ", comPhoto=" + comPhoto +
        ", comInfor=" + comInfor +
        ", userId=" + userId +
        "}";
    }
}
