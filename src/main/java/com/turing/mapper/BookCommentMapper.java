package com.turing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.turing.entity.BookComment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年02月09日 17:06:52
 */
@Mapper
public interface BookCommentMapper extends BaseMapper<BookComment> {
}
