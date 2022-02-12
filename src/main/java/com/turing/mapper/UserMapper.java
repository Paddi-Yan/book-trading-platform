package com.turing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.turing.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月20日 13:43:25
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
