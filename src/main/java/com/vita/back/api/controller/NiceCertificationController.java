package com.vita.back.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vita.back.api.model.Constant;
import com.vita.back.api.model.VitaResponse;
import com.vita.back.api.model.request.NiceCertificateAuthRequest;
import com.vita.back.api.model.request.NiceCertificationRequest;
import com.vita.back.api.service.NiceCertificationService;
import com.vita.back.common.exception.VitaException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "나이스 인증관련 API", description = "")
@RestController
@RequestMapping(value = "/nice")
public class NiceCertificationController {
    
    private final NiceCertificationService service;

    public NiceCertificationController(NiceCertificationService service) {
        this.service = service;
    }

    @Operation(summary = "nice 인증", description = "회원가입 nice 인증")
    @PostMapping("/user-join-certification")
    public VitaResponse<?> userJoinCertification(@RequestBody NiceCertificationRequest request) throws VitaException {
        return new VitaResponse<>(Constant.SUCCESS, service.userJoinCertification(request));
    }

    @Operation(summary = "nice 인증결과", description = "회원가입 nice 인증결과")
    @PostMapping("/user-join-certificate-auth")
    public VitaResponse<?> userJoinCertificateAuth(@RequestBody NiceCertificateAuthRequest request) throws VitaException {
        return new VitaResponse<>(Constant.SUCCESS, service.userJoinCertificateAuth(request));
    }
}
