package com.vita.back.api.model.data;

import lombok.Data;

@Data   
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
}
