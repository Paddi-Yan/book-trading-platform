package com.turing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.turing.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月22日 19:12:37
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite>
{
    @Select("SELECT f.*,b.* FROM `favorite` f LEFT JOIN `book` b ON f.book_id = b.id  WHERE f.user_id = ${userId} ORDER BY f.created_time DESC")
    @ResultMap("FavoriteResultMap")
    List<Favorite> selectFavoriteList(Integer userId);
}
