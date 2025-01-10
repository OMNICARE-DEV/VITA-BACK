package com.hops.hops_new_api.common.model.data;

import com.hops.hops_new_api.config.CustomToStringStyle;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter
@Setter
public class UserDto {

    private int userNo;
    private String customerName;
    private String userId;
    private String address;
    private String addressDetail;
    private String zipCd;
    private String phoneNo;
    private String mobileNo;
    private String birthday;
    private String userName;
    private String email;
    private String genderCd;
    private String loginPassword;
    private String serviceTermsOfUse;
    private String privacyPolicy;
    private String joinDt;
    private String userSt;
    private String customerId;
    private String userCi;
    private String userDi;
    private String lastPasswordChangeDt;
    private String agreeTermsList;
    private String marketingAgrYn;
    private String dormantYn;
    private String loginDt;



    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
    }

}