package com.turing.entity.elasticsearch;

import com.turing.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月28日 14:49:14
 */
@Data
@Document(indexName = "user")
public class UserDoc implements Serializable
{
    private static final long serialVersionUID = -6969673532311086756L;
    private Long id;
    private String nickname;
    private String gender;
    private String avatar;
    private String mobile;

    public void transform(User user)
    {
        BeanUtils.copyProperties(user,this);
    }

}
