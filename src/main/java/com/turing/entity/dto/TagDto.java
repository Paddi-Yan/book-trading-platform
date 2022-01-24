package com.turing.entity.dto;

import com.turing.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月21日 23:48:53
 */
@Data
@AllArgsConstructor
public class TagDto implements Serializable
{
    private static final long serialVersionUID = 3732396808614348602L;
    private List<Tag> publicTags;
    private List<Tag> privateTags;
}
