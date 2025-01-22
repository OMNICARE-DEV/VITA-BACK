package com.vita.back.api.controller;

import org.springframework.web.bind.annotation.*;

import com.vita.back.api.model.Constant;
import com.vita.back.api.model.VitaResponse;
import com.vita.back.api.model.request.RegCommonUserRequest;
import com.vita.back.api.model.request.UserLoginRequest;
import com.vita.back.api.model.response.UserLoginResponse;
import com.vita.back.api.service.UserLoginService;
import com.vita.back.common.exception.VitaCode;
import com.vita.back.common.exception.VitaException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "로그인/가입 API", description = "")
@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserLoginController {
    private final UserLoginService service;

    public UserLoginController(UserLoginService service) {
        this.service = service;
    }

    /** 통합로그인 아이디 로그인 */
    @Operation(summary = "로그인", description = "로그인 정보 조회")
    @PostMapping("/common-id-login")
    public VitaResponse<?> commonIdLogin(@RequestBody UserLoginRequest request) throws VitaException {
        log.info("UserLoginController.commonIdLogin request: {}", request);
        return new VitaResponse<>(Constant.SUCCESS, service.getUserLoginResponse(request));
    }

    /** 통합로그인 아이디 중복체크 */
    @Operation(summary = "중복체크", description = "아이디 중복체크")
    @GetMapping("/common-id-duplicate-check")
    public VitaResponse<?> commonIdDupCheck(@RequestParam(value = "userId") String userId,
                                            @RequestParam(value = "userCertifyNo") String userCertifyNo) throws VitaException {
        UserLoginRequest request = new UserLoginRequest();
        request.setUserId(userId);
        request.setUserCertifyNo(userCertifyNo);
        log.info("UserLoginController.commonIdDupCheck reqest: {}", request);
        return new VitaResponse<>(Constant.SUCCESS, service.userIdDupCheck(request));
    }

    /** 통합로그인 회원가입 */
    @Operation(summary = "통합로그인 회원가입", description = "회원가입")
    @PostMapping("/insert-common-user")
    public VitaResponse<?> insertCommonUser(@RequestBody RegCommonUserRequest request) throws VitaException {
        log.info("UserLoginController.regCommonUser request: {}", request);
        return new VitaResponse<>(Constant.SUCCESS, service.insertCommonUser(request));
    }

}
