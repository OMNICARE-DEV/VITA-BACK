package com.vita.back.api.controller;

import com.vita.back.api.model.Constant;
import com.vita.back.api.model.VitaResponse;
import com.vita.back.api.model.data.SendEmailDto;
import com.vita.back.api.model.request.NiceCertificateAuthRequest;
import com.vita.back.api.service.EmailService;
import com.vita.back.common.exception.VitaException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "EMAIL TEST", description = "이메일 발송 테스트")
@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService service;

    public EmailController(EmailService service) {
        this.service = service;
    }

    @Operation(summary = " 메일 발송 테스트 API")
    @PostMapping("/send-auth")
    public VitaResponse<?> sendAuthEmail(@RequestBody SendEmailDto request) throws VitaException {
        return new VitaResponse<>(Constant.SUCCESS, service.sendEmail(request));
    }
}