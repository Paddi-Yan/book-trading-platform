package com.turing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.turing.entity.dto.PostDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
@ApiModel(value = "Post", description = "帖子信息")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("帖子id")
    @TableId(value = "post_id", type = IdType.AUTO)
    private Long postId;

    @ApiModelProperty("社区id")
    private Long communityId;

    @ApiModelProperty("发表人id")
    private Long userId;

    @ApiModelProperty("相关书籍id")
    private Integer bookId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("帖子内容")
    private String content;

    @ApiModelProperty(hidden = true)
    private String photo;


    @ApiModelProperty("0-普通，1-置顶")
    private Integer type;

    private LocalDateTime createTime;


    public Long getPostId () {
        return postId;
    }

    public void setPostId (Long postId) {
        this.postId = postId;
    }

    public Long getCommunityId () {
        return communityId;
    }

    public void setCommunityId (Long communityId) {
        this.communityId = communityId;
    }

    public Long getUserId () {
        return userId;
    }

    public void setUserId (Long userId) {
        this.userId = userId;
    }

    public Integer getBookId () {
        return bookId;
    }

    public void setBookId (Integer bookId) {
        this.bookId = bookId;
    }

    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public String getContent () {
        return content;
    }

    public void setContent (String content) {
        this.content = content;
    }

    public Integer getType () {
        return type;
    }

    public void setType (Integer type) {
        this.type = type;
    }

    public LocalDateTime getCreateTime () {
        return createTime;
    }

    public void setCreateTime (LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString () {
        return "Post{" + "postId=" + postId + ", communityId=" + communityId + ", userId=" + userId + ", bookId=" + bookId + ", title=" + title + ", content=" + content + ", type=" + type + ", createTime=" + createTime + "}";
    }

    public void transform (PostDto postDto) {
        BeanUtils.copyProperties(postDto, this);
        StringBuilder photoString = new StringBuilder();
        List<String> photoList = postDto.getPhotoList();
        for (int i = 0; photoList != null && i < photoList.size(); i++) {
            if (i < photoList.size() - 1) {
                photoString.append(photoList.get(i) + ",");
            } else {
                photoString.append(photoList.get(i));
            }
        }
        this.setPhoto(photoString.toString());
    }
}
