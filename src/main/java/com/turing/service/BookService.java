package com.turing.service;

import com.turing.common.Result;
import com.turing.entity.dto.BookDto;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月21日 16:03:57
 */
public interface BookService {
    /**
     * 上传图书信息
     *
     * @return
     */
    Result uploadBookInfo (BookDto bookDto);

    Result getBookInfo ();

    Result getBookInfoByBookId (Integer bookId);

    Result getBookInfoByTag (Integer tag);

    Result getBookInfoByISBN (String ISBN);
}
