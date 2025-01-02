package com.hops.hops_new_api.common.mapper;

import com.hops.hops_new_api.common.model.Request.UserLoginRequest;
import com.hops.hops_new_api.common.model.Response.UserLoginResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserLoginMapper {
    public List<UserLoginResponse.CustomerMapDto> getCommonUser(UserLoginRequest request);
}
