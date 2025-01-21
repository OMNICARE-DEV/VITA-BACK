package com.vita.back.common.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
@Aspect
@Component
@Slf4j
public class LogAspect {

    private final ObjectMapper objectMapper;

    public LogAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Around("execution(* com.vita.back.api.controller..*(..))")
    public Object logRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        // 시작 시간
        long startTime = System.currentTimeMillis();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        log.info(">>>>>>>>>>>>>>>>>>>>> Request & Response Log <<<<<<<<<<<<<<<<<<<<<<");

        // 요청 정보
        log.info("Request INFO [{}] {}", request.getMethod(), request.getRequestURI());

        // 요청 헤더
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        log.info("Request Headers: {}", objectMapper.writeValueAsString(headers));

        // 요청 파라미터
        Map<String, String[]> parameters = request.getParameterMap();
        if (!parameters.isEmpty()) {
            log.info("Request Params: {}", objectMapper.writeValueAsString(parameters));
        }

        // 요청 본문
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
            String requestBody = new String(cachingRequest.getContentAsByteArray(), StandardCharsets.UTF_8);
            if (!requestBody.isEmpty()) {
                log.info("Request Body: {}", requestBody);
            }
        }

        // 메서드 실행
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable ex) {
            throw ex;
        }

        // 종료 시간
        long endTime = System.currentTimeMillis();
        // 실행 시간
        long executionTime = endTime - startTime;

        // 응답 - 답
        if (response != null) {
            log.info("Response Status Code: {}", response.getStatus());
        }
        log.info("Response Body: {}", objectMapper.writeValueAsString(result));
        log.info("Execution Time: {} ms", executionTime);

        log.info(">>>>>>>>>>>>>>>>>>>>> End of Log <<<<<<<<<<<<<<<<<<<<<<");

        return result;
    }
}