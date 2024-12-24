package com.hops.hops_new_api.common.exception;

import com.hops.hops_new_api.common.model.Constant;
import com.hops.hops_new_api.common.model.HopsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HopsExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(HopsExceptionHandler.class);

    /** BizException : 430*/
    @ExceptionHandler(HopsException.class)
    public ResponseEntity<HopsResponse<?>> handleBizException(HopsException ex) {
        return new ResponseEntity<HopsResponse<?>>(new HopsResponse<>(Constant.FAIL, ex.getCode(), ex.getMessage()),
                HttpStatusCode.valueOf(430));
    }

    /** Exception : 500 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HopsResponse<?>> handleException(Exception ex) {
        logger.error("[[[[[[[[[[[[[[[[ HopsExceptionHandler Error ]]]]]]]]]]]]]]]]]", ex);
        return new ResponseEntity<HopsResponse<?>>(
                new HopsResponse<>(Constant.FAIL, HopsCode.SYSTEM_ERROR.getCode(), HopsCode.SYSTEM_ERROR.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
