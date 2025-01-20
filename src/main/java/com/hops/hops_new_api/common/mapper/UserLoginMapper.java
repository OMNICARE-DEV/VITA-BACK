package com.hops.hops_new_api.common.mapper;

import com.hops.hops_new_api.common.model.Request.UserLoginRequest;
import com.hops.hops_new_api.common.model.Response.UserLoginResponse;
import com.hops.hops_new_api.common.model.data.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    int regCustomerMap(List<CustomerMapDto> customerMaps);

    UserLoginResponse getUserLoginResponse(int commonUserNo);

    List<CustomerMapDto> getMappingCustomerUser(String userCi);

    void updateCommonUserLoginDt(int commonUserNo);

    void updateUserLoginDt(int commonUserNo);
}
