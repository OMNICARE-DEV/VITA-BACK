package com.hops.hops_new_api.common.model.data;

import com.hops.hops_new_api.config.CustomToStringStyle;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter
@Setter
public class CommonUserDto {

    private int commonUserNo;
    private String userId;
    private String loginPassword;
    private String mobileNo;
    private String birthday;
    private String genderCd;
    private String userName;
    private String email;
    private String address;
    private String addressDetail;
    private String zipCd;
    private String serviceTermsOfUse;
    private String privacyPolicy;
    private String joinDt;
    private String userSt;
    private String phoneNo;
    private String userCi;
    private String userDi;
    private String domesticYn;
    private String lastPasswordChangeData;
    private String agreeTermsList;
    private String pwdResetDt;
    private String marketingAgrYn;
    private String lastPasswordChangeDt;
    private String dormantYn;
    private String loginDt;
    private String ssoCommonUserYn;
    private String ssoFirstUserYn;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
    }
}
