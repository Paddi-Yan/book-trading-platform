package com.turing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.turing.entity.dto.BookDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月21日 17:47:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book
{
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String name;

    private String photo;

    @TableField(value = "tag_id")
    private String tagId;


    private String description;

    /**
     * 图书类型
     * 0表示出书
     * 1表示求书
     */
    private Integer type;

    @TableField(value = "user_id")
    private Integer userId;

    @TableField(value = "created_time")
    private Timestamp createdTime;

    private Double price;

    /**
     * 图书当前状态
     * 0-已失效
     * 1-有效
     */
    private Integer status;

    public void transform(BookDto bookDto)
    {
        BeanUtils.copyProperties(bookDto,this);
        StringBuilder photoString = new StringBuilder();
        StringBuilder tagIdString = new StringBuilder();
        List<String> photoList = bookDto.getPhotoList();
        List<String > tagIdList = bookDto.getTagIdList();
        for (int i = 0; i < photoList.size(); i++) {
            if (i < photoList.size() - 1)
            {
                photoString.append(photoList.get(i)+",");
            }else
            {
                photoString.append(photoList.get(i));
            }
        }
        for (int i = 0; i < tagIdList.size(); i++) {
            if (i < tagIdList.size() - 1)
            {
                tagIdString.append(tagIdList.get(i) + ",");
            }else
            {
                tagIdString.append(tagIdList.get(i));
            }
        }
        this.setPhoto(photoString.toString());
        this.setTagId(tagIdString.toString());
    }
}
