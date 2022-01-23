package com.turing.service;

import com.turing.common.Result;
import com.turing.entity.Address;

import java.util.List;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月22日 23:38:00
 */
public interface AddressService
{

    Result addAddress(Address address);

    Result getAddress(Integer userId);

    Result editAddress(Address address);

    Result deleteAddress(Integer userId, List<Integer> addressIdList);
}
