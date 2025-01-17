package com.vita.back.common.exception;

import lombok.Getter;

@Getter
public enum VitaCode {
    /* 공통 */
    SUCCESS("VS0000", "정상처리"),
    REQUIRED_VALUE_FAIL("VE0001", "필수값 누락"),
    DATABASE_ERROR("VE0002", "DB 오류"),
    INVALID_PARAMETER("VE0003", "유효하지 않은 데이터"),
    AES_ERROR("VE0004", "암호화 실패"),
    DEC_ERROR("VE0005", "복호화 실패"),
    CERTIFICATE_ERROR("VE0006", "본인인증 오류"),
    REG_B2C_USER_ERROR("VE0007", "개인회원 등록 오류"),

    FORMAT_ERROR("VE9996", "데이터 포맷 오류"),
    PARSING_ERROR("VE9997", "파싱 오류"),
    NETWORK_ERROR("VE9998", "통신 오류"),
    SYSTEM_ERROR("VE9999", "시스템 오류"),

    /* USER_ERROR */
    FAIL_LOGIN("VU0001","회원 정보가 불일치 합니다."),
    CERTIFICATE_TIME_ERROR("VU0002", "본인인증 시간이 초과되었습니다."),
    ID_DUPLICATE_ERROR("VU0003", "이미 사용중이거나 탈퇴한 아이디 입니다."),
    JOIN_AGE_ERROR("VU0004", "만 14세 이하는 가입이 불가합니다."),
    ALREADY_JOIN_USER("VU0005", "이미 가입된 회원입니다.");

    private String code;
    private String message;

    VitaCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
