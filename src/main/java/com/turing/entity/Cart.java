package com.turing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月30日 15:47:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Cart", description = "购物车信息")
public class Cart {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Integer bookId;
    private String bookName;
    private String bookPhoto;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addTime;
    private int deleted;
    private BigDecimal price;
    private Integer count;
    private BigDecimal total;

    public void transform (User user, Book book) {
        this.setUserId(user.getId());
        this.setBookId(book.getId());
        this.setBookName(book.getTitle());
        this.setBookPhoto(book.getCover());
        this.setPrice(book.getSellingPrice());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(simpleDateFormat.format(new Date()), new ParsePosition(0));
        this.setAddTime(date);

    }
}
