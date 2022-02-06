package com.turing.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.turing.entity.Book;
import com.turing.entity.Post;
import io.swagger.annotations.ApiModel;
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
    private List<String> photoList;


    public void transform(Post post)
    {
        BeanUtils.copyProperties(post,this);
        List<String> photos = new ArrayList<>();
        if (post.getPhoto() == null) return;
        photos = Arrays.asList(post.getPhoto().split(","));
        this.setPhotoList(photos);

    }

}
