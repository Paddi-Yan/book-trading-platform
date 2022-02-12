package com.turing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.turing.entity.dto.BookCommentDto;
import com.turing.utils.BeanListUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年02月09日 12:24:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookComment {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 评论图片
     */
    private String photo;

    /**
     * 对应书籍ID下的评论
     */
    private Integer bookId;

    /**
     * 评论内容
     */
    private String content;
    /**
     * 对应的评论用户
     */
    private Long userId;

    /**
     * 评价类型 差评-0 中评-1 好评-2
     */
    private Integer type;

    /**
     * 评论标签
     */
    private String commentTag;

    public void transform (BookCommentDto bookCommentDto) {
        BeanUtils.copyProperties(bookCommentDto, this);
        this.setPhoto(BeanListUtils.transform(bookCommentDto.getPhoto()));
        this.setCommentTag(BeanListUtils.transform(bookCommentDto.getCommentTag()));
    }
}
