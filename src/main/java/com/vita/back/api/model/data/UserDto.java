package com.vita.back.api.model.data;

import lombok.Data;

@Data
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
}