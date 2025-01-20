package com.vita.back.api.model.request;

import lombok.Data;

@Data
public class RegCommonUserRequest {
    private String userCertifyNo;
    private String mobileNo;
    private String phoneNo;
    private String email;
    private String address;
    private String addressDetail;
    private String zipCd;
    private String userName;
    private String birthday;
    private String loginPassword;
    private String domesticYn;
    private String userId;
    private boolean serviceTermsOfUse;
    private boolean privacyPolicy;
    private boolean marketingAgree;
}
