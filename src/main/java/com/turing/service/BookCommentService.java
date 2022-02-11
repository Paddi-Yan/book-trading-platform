package com.turing.service;

import com.turing.common.Result;
import com.turing.entity.dto.BookCommentDto;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年02月09日 17:07:37
 */
public interface BookCommentService
{

    Result comment(BookCommentDto bookCommentDto);

    Result getCommentByPage(Integer page, Integer size, Integer bookId);

}
