package com.hops.hops_new_api.common.mapper;

import com.hops.hops_new_api.common.model.data.ServiceInfoDto;
import com.hops.hops_new_api.common.model.data.UserCertifyDto;
import org.apache.ibatis.annotations.Mapper;

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
}
