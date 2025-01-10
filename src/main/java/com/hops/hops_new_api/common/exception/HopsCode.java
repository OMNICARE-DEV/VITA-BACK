package com.hops.hops_new_api.common.exception;

import lombok.Getter;

@Getter
public enum HopsCode {
    //공통
    SUCCESS("HS0000", "정상처리"),
    REQUIRED_VALUE_FAIL("HE0001", "필수값 누락"),
    DATABASE_ERROR("HE0002", "DB 오류"),
    INVALID_PARAMETER("HE0003", "유효하지 않은 데이터"),
    AES_ERROR("HE0004", "암호화 실패"),
    DEC_ERROR("HE0005", "복호화 실패"),
    CERTIFICATE_ERROR("HE0006", "본인인증 오류"),
    REG_B2C_USER_ERROR("HE0007", "개인회원 등록 오류"),

    FORMAT_ERROR("HE9996", "데이터 포맷 오류"),
    PARSING_ERROR("HE9997", "파싱 오류"),
    NETWORK_ERROR("HE9998", "통신 오류"),
    SYSTEM_ERROR("HE9999", "시스템 오류"),

    //HOPS_USER_ERROR
    FAIL_LOGIN("HUE0001","회원 정보가 불일치 합니다."),
    CERTIFICATE_TIME_ERROR("HUE0002", "본인인증 시간이 초과되었습니다."),
    ID_DUPLICATE_ERROR("HUE0003", "이미 사용중이거나 탈퇴한 아이디 입니다."),
    JOIN_AGE_ERROR("HUE0004", "만 14세 이하는 가입이 불가합니다."),
    ALREADY_JOIN_USER("HUE0005", "이미 가입된 회원입니다.");


    private String code;
    private String message;

    HopsCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
