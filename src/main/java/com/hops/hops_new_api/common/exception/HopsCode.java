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
    FORMAT_ERROR("HE9996", "데이터 포맷 오류"),
    PARSING_ERROR("HE9997", "파싱 오류"),
    NETWORK_ERROR("HE9998", "통신 오류"),
    SYSTEM_ERROR("HE9999", "시스템 오류"),

    //HOPS_USER_ERROR
    FAIL_LOGIN("HUE0001","회원 불일치");

    private String code;
    private String message;

    HopsCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
