package com.turing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.turing.entity.QuestionAndAnswer;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月29日 14:14:03
 */
@Mapper
public interface QAMapper extends BaseMapper<QuestionAndAnswer> {
}
