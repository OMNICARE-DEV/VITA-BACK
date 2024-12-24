package com.hops.hops_new_api.common.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HopsResponse<T> {

    public HopsResponse(boolean result) {
        this.result = result;
    }

    public HopsResponse(boolean result, T data) {
        this.result = result;
        this.data = data;
    }

    public HopsResponse(boolean result, String code, String message) {
        this.result = result;
        this.code = code;
        this.message = message;
    }

    /** 결과 */
    private boolean result;
    /** 코드 */
    private String code;
    /** 메세지 */
    private String message;

    /** 응답 파라미터 모델 */
    private T data;
}
