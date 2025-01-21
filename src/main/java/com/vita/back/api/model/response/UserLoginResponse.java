package com.vita.back.api.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * 통합로그인 응답
 */
@Data
public class UserLoginResponse {
    /** 통합 회원번호*/
    @Schema(description = "통합 회원번호")
    private String commonUserNo;
    /** 이름*/
    @Schema(description = "이름")
    private String userName;
    /** 성별*/
    @Schema(description = "성별")
    private String genderCd;
    /** 전체 예약 수*/
    @Schema(description = "전체 예약 수")
    private int reservCount;
}
