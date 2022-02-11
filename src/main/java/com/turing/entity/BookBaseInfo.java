package com.turing.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年02月09日 13:55:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//@TableName(value = "book_base_info")
public class BookBaseInfo
{
    /**
     * ISBN编码
     */
    @TableField("isbn")
    private String ISBN;
    /**
     * 书名
     */
    private String title;
    /**
     * 书籍封面
     */
    private String cover;
    /**
     * 摘要
     */
    private String summary;
    /**
     * 出版社
     */
    private String publisher;
    /**
     * 出版时间按
     */
    private String  pubdate;
    /**
     * 作者
     */
    private String author;
    /**
     * 原价
     */
    private BigDecimal price;
    /**
     * 装帧
     */
    private String binding;
}
