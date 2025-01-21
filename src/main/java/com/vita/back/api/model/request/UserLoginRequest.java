package com.vita.back.api.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * 통합로그인 요청
 */
@Data
public class UserLoginRequest {
    /** 로그인타입*/
    @Schema(description = "로그인타입")
    private String loginType; //10:통합아이디로그인 20:사번로그인 21:사업장아이디로그인
    /** 로그인 ID*/
    @Schema(description = "로그인 ID")
    private String userId;
    /** 고객사 ID*/
    @Schema(description = "고객사 ID")
    private String customerId;
    /** 임직원 사번*/
    @Schema(description = "임직원 사번")
    private String employNo;
    /** 로그인 비밀번호*/
    @Schema(description = "로그인 비밀번호")
    private String loginPassword;
    /** 회원 인증번호*/
    @Schema(description = "회원 인증번호")
    private String userCertifyNo;
}
