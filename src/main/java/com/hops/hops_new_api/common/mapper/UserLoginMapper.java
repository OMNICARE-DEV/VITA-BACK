package com.hops.hops_new_api.common.mapper;

import com.hops.hops_new_api.common.model.Request.UserLoginRequest;
import com.hops.hops_new_api.common.model.Response.UserLoginResponse;
import com.hops.hops_new_api.common.model.data.CustomerMapDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserLoginMapper {
    public List<CustomerMapDto> getCommonUser(UserLoginRequest request);
}
