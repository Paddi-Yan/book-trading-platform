package com.turing.entity.dto;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.turing.entity.BookComment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年02月09日 17:10:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "BookCommentDto",description = "书籍评论信息")
public class BookCommentDto
{
    @ApiModelProperty(hidden = true)
    private Integer id;

    @ApiModelProperty(hidden = true)
    private List<String> photo;

    @ApiModelProperty(required = true)
    private Integer bookId;
    @ApiModelProperty(required = true)
    private String content;

    @ApiModelProperty(required = true)
    private Long userId;

    @ApiModelProperty(required = true,name = "type",value = "评价类型 差评-0 中评-1 好评-2")
    private Integer type;

    private List<String> commentTag;

    public void transform(BookComment comment)
    {
        BeanUtils.copyProperties(comment,this);
        if (StringUtils.isNotBlank(comment.getPhoto()))
        {
            this.setPhoto(Arrays.asList(comment.getPhoto().split(",")));
        }
        if (StringUtils.isNotBlank(comment.getCommentTag()))
        {
            this.setPhoto(Arrays.asList(comment.getCommentTag().split(",")));
        }
    }
}
