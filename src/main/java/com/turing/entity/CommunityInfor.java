package com.turing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.sql.Blob;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author qds
 * @since 2022-01-26
 */
@TableName("community_infor")
@ApiModel(value = "CommunityInfor对象", description = "")
public class CommunityInfor implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("社区id")
    @TableId(value = "com_id", type = IdType.AUTO)
    private Long comId;

    @ApiModelProperty("社区名称")
    private String comName;

    @ApiModelProperty("社区头像")
    private Blob comPhoto;

    @ApiModelProperty("社区信息介绍")
    private String comInfor;


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

    public Blob getComPhoto() {
        return comPhoto;
    }

    public void setComPhoto(Blob comPhoto) {
        this.comPhoto = comPhoto;
    }

    public String getComInfor() {
        return comInfor;
    }

    public void setComInfor(String comInfor) {
        this.comInfor = comInfor;
    }

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
