package com.vita.back.api.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * 통합로그인 회원가입 응답
 */
@Data
public class RegCommonUserResponse {
    /** 회원가입 성공여부*/
    @Schema(description = "회원가입 성공여부")
    public boolean joinSuccess;
}
