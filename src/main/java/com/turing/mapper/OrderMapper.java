package com.turing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.turing.entity.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年02月10日 11:05:13
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
