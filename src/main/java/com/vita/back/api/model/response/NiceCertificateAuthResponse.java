package com.vita.back.api.model.response;

import lombok.Data;

@Data
public class NiceCertificateAuthResponse {
    private String certifySuccessYn;
    private int userCertifyNo;
    private int userNo;
    private int commonUserNo;

    private String mobileNo;
    private String userCi;
    
    private String customerName;
    private String userId;
    private String address;
    private String addressDetail;
    private String phoneNo;
    private String zipCd;
    private String email;
    private String userName;
    private String birthday;
    private String gender;
}
