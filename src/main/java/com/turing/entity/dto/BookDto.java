package com.turing.entity.dto;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.turing.entity.Book;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月21日 15:43:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "BookDto", description = "书籍信息")
public class BookDto implements Serializable {
    private static final long serialVersionUID = 3036528493640743015L;

    @ApiModelProperty(hidden = true)
    private Integer id;
    /**
     * ISBN编码
     */
    @ApiModelProperty(required = true)
    private String ISBN;
    /**
     * 书名
     */
    @ApiModelProperty(hidden = true)
    private String title;
    /**
     * 书籍封面
     */
    @ApiModelProperty(hidden = true)
    private String cover;
    /**
     * 摘要
     */
    @ApiModelProperty(hidden = true)
    private String summary;
    /**
     * 出版社
     */
    @ApiModelProperty(hidden = true)
    private String publisher;
    /**
     * 出版时间
     */
    @ApiModelProperty(hidden = true)
    private String pubdate;
    /**
     * 作者
     */
    @ApiModelProperty(hidden = true)
    private String author;
    /**
     * 库存数量
     */
    @ApiModelProperty(required = true)
    private Integer stock;

    /**
     * 售价
     */
    private BigDecimal sellingPrice;

    /**
     * 邮费
     */
    @ApiModelProperty(required = false)
    private BigDecimal freight;

    /**
     * 发货地址
     */
    @ApiModelProperty(required = true)
    private Integer addressId;
    /**
     * 评价数
     */
    @ApiModelProperty(hidden = true)
    private Integer commentCount;
    /**
     * 好评率
     */
    @ApiModelProperty(hidden = true)
    private String activeRate;
    /**
     * 装帧
     */
    @ApiModelProperty(hidden = true)
    private String binding;
    /**
     * 书籍图片
     */
    @ApiModelProperty(hidden = true)
    private List<String> photoList;
    /**
     * 分类标签
     */
    @ApiModelProperty(required = true)
    private List<String> tagIdList;
    @ApiModelProperty(hidden = true)
    private List<String> commentTagList;

    @ApiModelProperty(required = true)
    private Integer userId;

    @ApiModelProperty(hidden = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createdTime;

    public void transform (Book book) {
        BeanUtils.copyProperties(book, this);
        this.activeRate = book.getActiveRate() + "%";
        List<String> tags = new ArrayList<>();
        List<String> photos = new ArrayList<>();
        List<String> commentTagList = new ArrayList<>();
        if (StringUtils.isNotBlank(book.getPhoto())) {
            photos = Arrays.asList(book.getPhoto().split(","));
            this.setPhotoList(photos);
        }
        if (StringUtils.isNotBlank(book.getTagId())) {
            tags = Arrays.asList(book.getTagId().split(","));
            this.setTagIdList(tags);
        }
        if (StringUtils.isNotBlank(book.getCommentTag())) {
            commentTagList = Arrays.asList(book.getCommentTag().split(","));
            this.setCommentTagList(commentTagList);
        }
    }
}
