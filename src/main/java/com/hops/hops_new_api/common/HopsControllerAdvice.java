package com.hops.hops_new_api.common;

import com.hops.hops_new_api.common.exception.HopsCode;
import com.hops.hops_new_api.common.model.HopsResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class HopsControllerAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {
        
        if(body instanceof HopsResponse) {
            if(((HopsResponse) body).isResult()) {
                ((HopsResponse) body).setCode(HopsCode.SUCCESS.getCode());
                ((HopsResponse) body).setMessage(HopsCode.SUCCESS.getMessage());
            }
        }
        
        return body;
    }
}
