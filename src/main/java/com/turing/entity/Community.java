package com.turing.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
@ApiModel(value = "Community对象", description = "社区信息")
public class Community implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("社区id")
    private Long communityId;


    public Long getUserId () {
        return userId;
    }

    public void setUserId (Long userId) {
        this.userId = userId;
    }

    public Long getCommunityId () {
        return communityId;
    }

    public void setCommunityId (Long communityId) {
        this.communityId = communityId;
    }

    @Override
    public String toString () {
        return "Community{" + "userId=" + userId + ", communityId=" + communityId + "}";
    }
}
