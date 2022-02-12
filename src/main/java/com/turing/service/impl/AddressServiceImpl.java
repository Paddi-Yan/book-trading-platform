package com.turing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.turing.common.HttpStatusCode;
import com.turing.common.Result;
import com.turing.entity.Address;
import com.turing.mapper.AddressMapper;
import com.turing.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月22日 23:38:29
 */
@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;

    @Override
    public Result addAddress (Address address) {
        addressMapper.insert(address);
        return new Result().success(address);
    }

    @Override
    public Result getAddress (Integer userId) {
        List<Address> addressList = addressMapper.selectList(new QueryWrapper<Address>().eq("user_id", userId));
        return new Result().success(addressList);
    }

    @Override
    public Result editAddress (Address address) {
        Integer count = addressMapper.selectCount(new QueryWrapper<Address>().eq("id", address.getId()));
        if (count == 0) {
            return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("不存在该地址信息,修改失败!");
        }
        addressMapper.updateById(address);
        return new Result().success(address);
    }

    @Override
    public Result deleteAddress (Integer userId, List<Integer> addressIdList) {
        List<Address> addressList = addressMapper.selectBatchIds(addressIdList);
        for (Address address : addressList) {
            if (!userId.equals(address.getUserId())) {
                return new Result().fail(HttpStatusCode.REQUEST_PARAM_ERROR).message("该用户不存在该地址信息,无权限删除!");
            }
        }
        addressMapper.deleteBatchIds(addressIdList);
        return new Result().success(addressList);
    }
}
