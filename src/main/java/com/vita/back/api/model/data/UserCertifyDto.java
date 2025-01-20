package com.vita.back.api.model.data;

import lombok.Data;

@Data
public class UserCertifyDto {
    private int userCertifyNo;
    private String userCertifyDiv;
    private int userNo;
    private String mobileNo;
    private String checkupRosterNo;
    private String userCertifyType;
    private String certifyConfirmNo;
    private String regDt;
    private String certifySt;
    private String ci;
    private String di;
    private String userName;
    private String birthday;
    private String genderCd;
    private String domesticYn;
}
