package com.turing.service;

import com.turing.common.Result;
import com.turing.entity.Tag;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月21日 22:12:28
 */
public interface TagService {
    Result getAllTags (Integer userId);

    Result addTag (Integer userId, String tagName);

    Result deleteTag (Integer userId, Integer tagId);

    Result editTag (Tag tag);

    Result getPublicTags ();


}
