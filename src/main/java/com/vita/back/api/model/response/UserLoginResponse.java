package com.vita.back.api.model.response;

import lombok.Data;

@Data
public class UserLoginResponse {
    private String commonUserNo;
    private String userName;
    private String genderCd;
    private int reservCount;
}
