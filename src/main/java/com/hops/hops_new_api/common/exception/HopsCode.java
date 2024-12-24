package com.hops.hops_new_api.common.exception;

import lombok.Getter;

@Getter
public enum HopsCode {
    SUCCESS("HS0000", "정상처리"),
    FAIL_LOGIN("HE0001","회원 불일치"),


    FORMAT_ERROR("HE9996", "데이터 포맷 오류"),
    PARSING_ERROR("HE9997", "파싱 오류"),
    NETWORK_ERROR("HE9998", "통신 오류"),
    SYSTEM_ERROR("HE9999", "시스템 오류");

    private String code;
    private String message;

    HopsCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
