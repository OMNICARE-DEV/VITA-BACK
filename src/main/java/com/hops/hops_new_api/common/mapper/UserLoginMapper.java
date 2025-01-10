package com.hops.hops_new_api.common.mapper;

import com.hops.hops_new_api.common.model.Request.UserLoginRequest;
import com.hops.hops_new_api.common.model.Response.UserLoginResponse;
import com.hops.hops_new_api.common.model.data.CommonUserDto;
import com.hops.hops_new_api.common.model.data.UserCertifyDto;
import com.hops.hops_new_api.common.model.data.UserDto;
import com.hops.hops_new_api.common.model.data.UserIdDupCheckDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserLoginMapper {
    public Integer getCommonUserNo(UserLoginRequest request);

    String getUserCi(String userCertifyNo);

    int getCommonUserIdDupCheck(UserIdDupCheckDto userIdDupCheckDto);

    UserCertifyDto getCertifyInfoByKey(String userCertifyNo);

    void updtCertifySt(String userCertifyNo);

    int getCommonUserCount(UserIdDupCheckDto userCiDupCheckDto);

    void joinCommonUser(CommonUserDto request);

    UserDto getCommonUser(int commonUserNo);

    int regB2CUser(UserDto b2CUser);

    int checkB2CUser(int commonUserNo);

    int regCustomerMap(UserDto b2CUser);

    UserLoginResponse getUserLoginResponse(int commonUserNo);
}
