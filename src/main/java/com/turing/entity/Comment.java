package com.turing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@ApiModel(value = "Comment", description = "帖子信息")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("帖子id")
    private Long postId;

    private Long userId;

    private String content;

    private LocalDateTime createTime;


    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public Long getPostId () {
        return postId;
    }

    public void setPostId (Long postId) {
        this.postId = postId;
    }

    public Long getUserId () {
        return userId;
    }

    public void setUserId (Long userId) {
        this.userId = userId;
    }

    public String getContent () {
        return content;
    }

    public void setContent (String content) {
        this.content = content;
    }

    public LocalDateTime getCreateTime () {
        return createTime;
    }

    public void setCreateTime (LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString () {
        return "Comment{" + "id=" + id + ", postId=" + postId + ", userId=" + userId + ", content=" + content + ", createTime=" + createTime + "}";
    }
}
