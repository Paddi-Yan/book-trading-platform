package com.turing.entity.elasticsearch;

import com.turing.entity.Book;
import com.turing.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月28日 14:49:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "book")
public class BookDoc implements Serializable
{
    private static final long serialVersionUID = -3710952780775682734L;
    @Field(value = "id")
    private Integer id;
    private String name;
    private String photo;
    private List<String> tagId;
    private String description;
    private Integer type;
    private Integer userId;
    private Integer status;
    private BigDecimal price;

    public void transform(Book book)
    {
        BeanUtils.copyProperties(book,this);
        this.tagId = Arrays.asList(book.getTagId().split(","));
        this.status = 1;
    }
}
