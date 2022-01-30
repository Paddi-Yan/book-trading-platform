package com.turing.mapper;

import com.turing.entity.CommunityInfor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qds
 * @since 2022-01-26
 */
@Mapper
public interface CommunityInforMapper extends BaseMapper<CommunityInfor> {

//    @ResultMap("getCommunityByUserIdMap")
    List<CommunityInfor> getCommunityByUserId(Integer userId);
}
