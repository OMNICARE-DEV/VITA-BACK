package com.vita.back.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vita.back.api.model.Constant;
import com.vita.back.api.model.VitaResponse;

@RestControllerAdvice
public class VitaExceptionHandler {

    /** BizException : 430*/
    @ExceptionHandler(VitaException.class)
    public ResponseEntity<VitaResponse<?>> handleBizException(VitaException ex) {
        return new ResponseEntity<VitaResponse<?>>(new VitaResponse<>(Constant.FAIL, ex.getCode(), ex.getMessage()),
                HttpStatusCode.valueOf(430));
    }

    /** Exception : 500 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<VitaResponse<?>> handleException(Exception ex) {
        return new ResponseEntity<VitaResponse<?>>(
                new VitaResponse<>(Constant.FAIL, VitaCode.SYSTEM_ERROR.getCode(), VitaCode.SYSTEM_ERROR.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
