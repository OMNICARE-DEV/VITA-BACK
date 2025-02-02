package com.vita.back.api.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * 통합로그인 회원가입 요청
 */
@Data
public class RegCommonUserRequest {
    /** 인증 번호 */
    @Schema(description = "인증 번호")
    private String userCertifyNo;

    /** 휴대전화 번호 */
    @Schema(description = "사용자 휴대전화 번호")
    private String mobileNo;

    /** 전화번호 */
    @Schema(description = "사용자 전화번호")
    private String phoneNo;

    /** 이메일 주소 */
    @Schema(description = "사용자 이메일 주소")
    private String email;

    /** 기본 주소 */
    @Schema(description = "사용자 기본 주소")
    private String address;

    /** 상세 주소 */
    @Schema(description = "사용자 상세 주소")
    private String addressDetail;

    /** 우편번호 */
    @Schema(description = "우편번호")
    private String zipCd;

    /** 사용자 이름 */
    @Schema(description = "사용자 이름")
    private String userName;

    /** 생년월일 (YYYY-MM-DD) */
    @Schema(description = "생년월일 (YYYY-MM-DD 형식)")
    private String birthday;

    /** 로그인 비밀번호 */
    @Schema(description = "로그인 비밀번호")
    private String loginPassword;

    /** 내국인 여부 (Y/N) */
    @Schema(description = "내국인 여부 (Y/N)")
    private String domesticYn;

    /** 사용자 ID */
    @Schema(description = "사용자 ID")
    private String userId;

    /** 서비스 이용약관 동의 여부 */
    @Schema(description = "서비스 이용약관 동의 여부")
    private boolean serviceTermsOfUse;

    /** 개인정보 처리방침 동의 여부 */
    @Schema(description = "개인정보 처리방침 동의 여부")
    private boolean privacyPolicy;

    /** 마케팅 동의 여부 */
    @Schema(description = "마케팅 동의 여부")
    private boolean marketingAgree;
}