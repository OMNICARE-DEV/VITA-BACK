package com.hops.hops_new_api.common.mapper;

import com.hops.hops_new_api.common.model.Request.UserLoginRequest;
import com.hops.hops_new_api.common.model.Response.UserLoginResponse;
import com.hops.hops_new_api.common.model.data.CommonUserDto;
import com.hops.hops_new_api.common.model.data.UserCertifyDto;
import com.hops.hops_new_api.common.model.data.UserIdDupCheckDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserLoginMapper {
    public List<UserLoginResponse.CustomerMapDto> getCommonUser(UserLoginRequest request);

    String getUserCi(String userCertifyNo);

    int getCommonUserIdDupCheck(UserIdDupCheckDto userIdDupCheckDto);

    UserCertifyDto getCertifyInfoByKey(String userCertifyNo);

    void updtCertifySt(String userCertifyNo);

    int getCommonUserCount(UserIdDupCheckDto userCiDupCheckDto);

    void joinCommonUser(CommonUserDto request);
}
