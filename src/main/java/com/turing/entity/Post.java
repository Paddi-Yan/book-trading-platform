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
@ApiModel(value = "Post对象", description = "")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
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

    @ApiModelProperty(hidden = true)
    private Integer type;

    @ApiModelProperty(hidden = true)
    private LocalDateTime createTime;


    public void transform(PostDto postDto) {
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
