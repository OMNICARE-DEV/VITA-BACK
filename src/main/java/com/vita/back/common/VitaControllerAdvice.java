package com.vita.back.common;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.vita.back.api.model.VitaResponse;
import com.vita.back.common.exception.VitaCode;

@RestControllerAdvice
public class VitaControllerAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {
        
        if(body instanceof VitaResponse) {
            if(((VitaResponse<?>) body).isResult()) {
                ((VitaResponse<?>) body).setCode(VitaCode.SUCCESS.getCode());
                ((VitaResponse<?>) body).setMessage(VitaCode.SUCCESS.getMessage());
            }
        }
        
        return body;
    }
}
