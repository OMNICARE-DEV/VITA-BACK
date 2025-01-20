package com.vita.back.api.model.response;

import lombok.Data;

@Data
public class UserLoginResponse {
    private String secretKey;
    private String commonUserNo;
    private String userName;
    private String genderCd;
    private int reservCount;
    private String loginDt;
}
