package com.turing.service;

import com.turing.common.Result;
import com.turing.entity.Book;

import java.util.List;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月22日 19:13:24
 */
public interface FavoriteService {
    Result addFavorite (Integer userId, Book book);

    Result getFavorites (Integer userId);

    Result deleteFavorite (Integer userId, List<Integer> favoriteList);
}
