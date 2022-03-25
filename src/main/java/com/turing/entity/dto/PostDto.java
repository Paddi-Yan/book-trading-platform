package com.turing.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.turing.entity.Post;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
public class PostDto implements Serializable {

    @ApiModelProperty(hidden = true)
    @TableId(value = "post_id", type = IdType.AUTO)
    private Long postId;

    @ApiModelProperty("社区id")
    private Long communityId;

    @ApiModelProperty("发表人id")
    private Long userId;

    @ApiModelProperty(hidden = true)
    private String userName;

    @ApiModelProperty(hidden = true)
    private String headPhoto;

    @ApiModelProperty(hidden = true)
    private String address;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("帖子内容")
    private String content;

    @ApiModelProperty(hidden = true)
    private Integer type;

    @ApiModelProperty(hidden = true)
    private LocalDateTime createTime;

    @ApiModelProperty(hidden = true)
    private List<String> photoList;

    @ApiModelProperty(hidden = true)
    private Integer commentCount;

    @ApiModelProperty(hidden = true)
    private Integer likeCount;


    public void transform(Post post,String userName,String headPhoto,String address, Integer commentCount, Integer likeCount) {
        BeanUtils.copyProperties(post, this);
        this.setCommentCount(commentCount);
        this.setLikeCount(likeCount);
        this.setUserName(userName);
        this.setHeadPhoto(headPhoto);
        this.setAddress(address);

        List<String> photos = new ArrayList<>();
        if (post.getPhoto() == null) return;
        photos = Arrays.asList(post.getPhoto().split(","));
        this.setPhotoList(photos);
    }

}
