package com.vita.back.api.model.request;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String loginType; //10:통합아이디로그인 20:사번로그인 21:사업장아이디로그인
    private String userId;
    private String customerId;
    private String employNo;
    private String loginPassword;
    private String userCertifyNo;
}
