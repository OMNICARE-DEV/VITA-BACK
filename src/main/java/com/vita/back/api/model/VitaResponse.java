package com.vita.back.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * Vita API응답
 */
@Data
public class VitaResponse<T> {

    public VitaResponse(boolean result) {
        this.result = result;
    }

    public VitaResponse(boolean result, T data) {
        this.result = result;
        this.data = data;
    }

    public VitaResponse(boolean result, String code, String message) {
        this.result = result;
        this.code = code;
        this.message = message;
    }

    /** 결과 */
    @Schema(description = "결과")
    private boolean result;
    /** 코드 */
    @Schema(description = "코드")
    private String code;
    /** 메세지 */
    @Schema(description = "메세지")
    private String message;

    /** 응답 파라미터 모델 */
    @Schema(description = "응답 파라미터 모델")
    private T data;
}
