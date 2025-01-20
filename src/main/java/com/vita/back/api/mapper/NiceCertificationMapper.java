package com.vita.back.api.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.vita.back.api.model.data.ServiceInfoDto;
import com.vita.back.api.model.data.UserCertifyDto;
import com.vita.back.api.model.data.UserDto;

@Mapper
public interface NiceCertificationMapper {
    int regUserCertify(UserCertifyDto userCertifyDto);

    String getServiceStaticInfo(String clientId);

    void updtStTo80(int userCertifyNo);

    String getServiceInfo(String keyIdPhone);

    void updtServiceInfoRegDt(ServiceInfoDto serviceInfoDto);

    void updtIvPhone(String iv);

    void updtKeyPhone(String key);

    void updtHmacKeyPhone(String hmacKey);

    void updtSiteCodePhone(String siteCode);

    void updtTokenVersionIdPhone(String tokenVersionId);

    void updtIvIpin(String iv);

    void updtKeyIpin(String key);

    void updtHmacKeyIpin(String hmacKey);

    void updtSiteCodeIpin(String siteCode);

    void updtTokenVersionIdIpin(String tokenVersionId);

    void updateUserCertify(UserCertifyDto userCertifyDto);

    Integer getAlreadyCommonUserInfo(String ci);

    UserDto getAlreadyUserInfo(String ci);
}
