package com.vita.back.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.vita.back.api.model.data.CommonUserDto;
import com.vita.back.api.model.data.CustomerMapDto;
import com.vita.back.api.model.data.UserCertifyDto;
import com.vita.back.api.model.data.UserDto;
import com.vita.back.api.model.data.UserIdDupCheckDto;
import com.vita.back.api.model.request.UserLoginRequest;
import com.vita.back.api.model.response.UserLoginResponse;

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
