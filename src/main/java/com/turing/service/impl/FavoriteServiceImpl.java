package com.turing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.turing.common.HttpStatusCode;
import com.turing.common.Result;
import com.turing.entity.Book;
import com.turing.entity.Favorite;
import com.turing.mapper.FavoriteMapper;
import com.turing.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月22日 19:15:50
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public Result addFavorite (Integer userId, Book book) {
        Integer selectCount = favoriteMapper.selectCount(new QueryWrapper<Favorite>().eq("user_id", userId)
                .eq("book_id", book.getId()));
        if (selectCount == 1) {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("用户已收藏该书籍!");
        }
        Favorite favorite = new Favorite();
        favorite.setBookId(book.getId());
        favorite.setUserId(userId);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        favorite.setCreatedTime(Timestamp.valueOf(simpleDateFormat.format(new Date())));
        favoriteMapper.insert(favorite);
        favorite.setBook(book);
        return new Result().success(favorite);
    }

    @Override
    public Result getFavorites (Integer userId) {
        List<Favorite> favorites = favoriteMapper.selectFavoriteList(userId);
        return new Result().success(favorites);
    }

    @Override
    public Result deleteFavorite (Integer userId, List<Integer> favoriteList) {
        List<Favorite> favorites = favoriteMapper.selectBatchIds(favoriteList);
        for (Favorite favorite : favorites) {
            if (!userId.equals(favorite.getUserId())) {
                return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("该用户不存在该收藏信息,无权限删除!");
            }
        }
        favoriteMapper.deleteBatchIds(favoriteList);
        return new Result().success(favorites);
    }
}
