package com.turing.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.turing.entity.Book;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

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
public class BookDto implements Serializable
{
    private static final long serialVersionUID = 3036528493640743015L;

    @ApiModelProperty(hidden = true)
    private Integer id;

    @ApiModelProperty(required = true)
    private String name;
    /**
     * 书籍图片
     */
    @ApiModelProperty(hidden = true)
    private List<String> photoList;
    /**
     * 分类标签
     */
    @ApiModelProperty(required = true)
    private List<String > tagIdList;
    /**
     * 简介描述
     */
    @ApiModelProperty(required = true)
    private String description;

    @ApiModelProperty(required = true)
    private BigDecimal price;

    /**
     * 图书类型
     * 0表示出书
     * 1表示求书
     */
    @ApiModelProperty(name = "type",required = true)
    private Integer type;

    @ApiModelProperty(required = true)
    private Integer userId;

    @ApiModelProperty(hidden = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createdTime;

    public void transform(Book book)
    {
        BeanUtils.copyProperties(book,this);
        List<String > tags = new ArrayList<>();
        List<String> photos = new ArrayList<>();
        photos = Arrays.asList(book.getPhoto().split(","));
        tags = Arrays.asList(book.getTagId().split(","));
        this.setPhotoList(photos);
        this.setTagIdList(tags);
    }
}
