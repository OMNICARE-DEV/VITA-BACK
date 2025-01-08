package com.hops.hops_new_api.common.controller;

import com.hops.hops_new_api.common.exception.HopsException;
import com.hops.hops_new_api.common.model.Constant;
import com.hops.hops_new_api.common.model.HopsResponse;
import com.hops.hops_new_api.common.model.Request.NiceCertificateAuthRequest;
import com.hops.hops_new_api.common.model.Request.NiceCertificationRequest;
import com.hops.hops_new_api.common.service.NiceCertificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AUTH", description = "나이스 인증관련 API")
@RestController
@RequestMapping(value = "/nice")
@Slf4j
public class NiceCertificationController {
    private static final Logger logger = LoggerFactory.getLogger(NiceCertificationController.class);

    private final NiceCertificationService service;

    public NiceCertificationController(NiceCertificationService service) {this.service = service;}

    @Operation(summary = "nice 인증", description = "회원가입 nice 인증")
    @PostMapping("/userJoinCertification")
    public HopsResponse userJoinCertification(@RequestBody NiceCertificationRequest request) throws HopsException {
        logger.info("NiceCertificationController.userJoinCertification request: {}", request);
        return new HopsResponse<>(Constant.SUCCESS, service.userJoinCertification(request));
    }

    @Operation(summary = "nice 인증결과", description = "회원가입 nice 인증결과")
    @PostMapping("/userJoinCertificateAuth")
    public HopsResponse userJoinCertificateAuth(@RequestBody NiceCertificateAuthRequest request) throws HopsException {
        logger.info("NiceCertificationController.userJoinCertificateAuth request: {}", request);
        return new HopsResponse<>(Constant.SUCCESS, service.userJoinCertificateAuth(request));
    }
}
