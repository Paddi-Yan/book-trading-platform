package com.turing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.turing.entity.dto.BookDto;
import com.turing.utils.BeanListUtils;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月21日 17:47:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Book", description = "书籍信息")
public class Book extends BookBaseInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 书籍图片
     */
    private String photo;

    private String tagId;

    /**
     * 评价标签
     */
    private String commentTag;

    /**
     * 售价
     */
    private BigDecimal sellingPrice;

    private BigDecimal freight;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 发货地址
     */
    private Integer addressId;
    /**
     * 评价数
     */
    private Integer commentCount;
    /**
     * 好评率
     */
    private Double activeRate;

    @TableField(value = "user_id")
    private Integer userId;

    @TableField(value = "created_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Timestamp createdTime;

    /**
     * 图书当前状态
     * 0-已失效
     * 1-有效
     */
    private Integer status;

    public void transform (BookDto bookDto) {
        BeanUtils.copyProperties(bookDto, this);

        this.setPhoto(BeanListUtils.transform(bookDto.getPhotoList()));
        this.setTagId(BeanListUtils.transform(bookDto.getTagIdList()));
        this.setCommentTag(BeanListUtils.transform(bookDto.getCommentTagList()));
    }
}
